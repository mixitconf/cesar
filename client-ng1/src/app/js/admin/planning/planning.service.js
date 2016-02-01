(function () {

  'use strict';

  angular.module('cesar-planning').factory('PlanningService', function ($http, $q) {
    'ngInject';

    function getRoom(){
      return $http.get('/api/planning/room');
    }

    function getSlots(year){
      return $http.get('/api/planning' + (!!year ? '?year=' + year : ''));
    }

    /**
     * When we want to calculate the number of hour between 2 moments
     */
    function computeRange(moment, startTime, endTime){
      var start, end;
      var range = [];
      var time = {
        hour : startTime.hour,
        minute : startTime.minute
      };

      while(time.hour<endTime.hour){
        start = moment.clone();
        end = moment.clone();

        start.set('hour', time.hour);
        start.set('minute', time.minute);

        time.hour++;
        time.minute= 0;

        end.set('hour', time.hour);
        end.set('minute', time.minute);

        range.push({
          start : start,
          end : end
        });

        if(time.hour===endTime.hour && start.get('minute')!==endTime.minute){
          start = end;
          end = start.clone();
          end.set('minute', endTime.minute);

          range.push({
            start : start,
            end : end
          });
        }
      }

      return range;
    }

    function computeSlots(rooms, year){
      return getSlots(year).then(function(response){
        var slotsByRoom = response.data;

        if(rooms){
          rooms.forEach(function(room){
            //We see if slots exists
            if(!slotsByRoom[room.key]){
              slotsByRoom[room.key] = [];
            }

            var time = {
              hour:8,
              minute:0
            }

            while(time.hour<19){

              time.hour++;
            }
            for(var i=0 ; i<slotsByRoom[room.key].length ; i++){
              var elt = slotsByRoom[room.key][i];
              //console.log(elt.label)
              console.log(moment(elt.start, moment.ISO_8601))
              //if(elt.start)
            }
            //slotsByRoom[room.key].forEach(function(elt));
            //8 h à 19 h 11h
            //des creneaux de 5 minutes %60 12 par heures
          })
        }

        return $q.when(slotsByRoom);
      });
    }



    return {
      getRoom : getRoom,
      getSlots: getSlots,
      computeSlots: computeSlots,
      computeRange: computeRange
    };
  });
})();