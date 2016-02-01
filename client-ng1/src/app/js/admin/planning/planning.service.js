(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').factory('PlanningService', function ($http, $q) {
    'ngInject';

    function getRoom() {
      return $http.get('/api/planning/room');
    }

    function getSlots(year) {
      return $http.get('/api/planning' + (!!year ? '?year=' + year : ''));
    }

    /**
     * Set the moment
     */
    function setTime(moment, time){
      var myTime = moment.clone();
      myTime.set('hour', time.hour);
      myTime.set('minute', time.minute);
      myTime.set('second', 0);
      myTime.set('millisecond', 0);
      return myTime;
    }

    /**
     * This function computes the set of time slices available between two hours. The goal
     * is to return a table with a data for each new hour
     */
    function computeRange(moment, startTime, endTime) {
      var start, end;
      var range = [];

      var time = {
        hour: startTime.hour,
        minute: startTime.minute
      };

      if(time.hour === endTime.hour && time.minute !== endTime.minute){
        start = setTime(moment, time);
        end = setTime(moment, endTime);

        return [{
          start: start.toISOString(),
          end: end.toISOString()
        }];
      }

      while (time.hour < endTime.hour) {
        start = setTime(moment, time);
        time.hour++;
        time.minute = 0;
        end = setTime(moment, time);

        range.push({
          start: start.toISOString(),
          end: end.toISOString()
        });

        if (time.hour === endTime.hour && start.get('minute') !== endTime.minute) {
          start = end;
          end = start.clone();
          end.set('minute', endTime.minute);

          range.push({
            start: start.toISOString(),
            end: end.toISOString()
          });
        }
      }
      return range;
    }

    /**
     * Set the moment
     */
    function putRange(slots, ranges){
      if(ranges){
        ranges.forEach(function(range){
          slots.push(range);
        });
      }
    }

    /**
     * Slots are read in database and a table is build to be able to display a nice planning
     */
    function computeSlots(rooms, year) {
      return getSlots(year).then(function (response) {
        var slotsByRoom = response.data;

        if (rooms) {
          rooms.forEach(function (room) {
            //We see if slots exists for this room
            if (!slotsByRoom[room.key]) {
              slotsByRoom[room.key] = computeRange(moment(), { hour: 8, minute:0}, { hour: 19, minute:0});
            }
            else{
              var slots = [], elt, time;
              var lastTime = {
                hour: 8,
                minute:0
              };

              for(var i in slotsByRoom[room.key]){
                elt = slotsByRoom[room.key][i];

                time = {
                  hour: moment(elt.start).get('hour'),
                  minute: moment(elt.start).get('minute')
                };

                if(time.hour!==lastTime.hour || time.minute!==lastTime.minute){
                  putRange(slots, computeRange(moment(elt.start), lastTime, time));
                }

                slots.push(elt);

                lastTime = {
                  hour: moment(elt.end).get('hour'),
                  minute: moment(elt.end).get('minute')
                };
              }

              //For the last one we have to verify the last range
              if(lastTime.hour<19){
                putRange(slots, computeRange(moment(elt.start), lastTime, { hour: 19, minute:0}));
              }
              slotsByRoom[room.key] = slots;
            }
          });
        }
        return $q.when(slotsByRoom);
      });
    }


    return {
      getRoom: getRoom,
      getSlots: getSlots,
      computeSlots: computeSlots,
      computeRange: computeRange
    };
  });
})();