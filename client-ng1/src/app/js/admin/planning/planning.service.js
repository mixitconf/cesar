(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').factory('PlanningService', function ($http, $q) {
    'ngInject';

    var DATE_FORMAT = 'YYYY-MM-DD HH:mm';
    var DATE_FORMAT_ISO = 'YYYY-MM-DD HH:mm:ss';

    function getRoom() {
      return $http.get('/api/planning/room');
    }

    function convertDate(mydate) {
      return moment(mydate).format(DATE_FORMAT_ISO);
    }

    function getSlots(year) {
      return $http.get('/api/planning' + (!!year ? '?year=' + year : ''));
    }

    function getTransversalSlots(year) {
      return $http.get('/api/planning/transversal' + (!!year ? '?year=' + year : ''));
    }
    
    function getEventOutOfSession(){
      return [
        'view.planning.moment.close',
        'view.planning.moment.ligthning',
        'view.planning.moment.lunch',
        'view.planning.moment.random',
        'view.planning.moment.mixteen',
        'view.planning.moment.party',
        'view.planning.moment.pause',
        'view.planning.moment.staff',
        'view.planning.moment.session-pres',
        'view.planning.moment.welcome'
      ];
    }
    function _setTime(moment, time) {
      var myTime = moment.clone();
      myTime.set('hour', time.hour);
      myTime.set('minute', time.minute);
      myTime.set('second', 0);
      myTime.set('millisecond', 0);
      return myTime;
    }

    function _createSlot(start, end) {
      return {
        start: start.format(DATE_FORMAT),
        end: end.format(DATE_FORMAT),
        duration: moment.duration(end.diff(start)).as('minutes')
      };
    }

    /**
     * This function computes the set of time slices available between two hours. The goal
     * is to return a table with a data for each new hour
     */
    function computeRange(instant, startTime, endTime) {
      if (!startTime || !endTime) {
        instant = instant ? instant : moment();
        startTime = {hour: 8, minute: 0};
        endTime = {hour: 19, minute: 0};
      }
      var start, end;
      var range = [];

      var time = {
        hour: startTime.hour,
        minute: startTime.minute
      };

      if (time.hour === endTime.hour && time.minute !== endTime.minute) {
        start = _setTime(instant, time);
        end = _setTime(instant, endTime);
        return [_createSlot(start, end)];
      }

      while (time.hour < endTime.hour) {
        start = _setTime(instant, time);
        time.hour++;
        time.minute = 0;
        end = _setTime(instant, time);

        range.push(_createSlot(start, end));

        if (time.hour === endTime.hour && time.minute !== endTime.minute) {
          start = _setTime(instant, time);
          time.minute = endTime.minute;
          end = _setTime(instant, time);

          range.push(_createSlot(start, end));
        }
      }
      return range;
    }

    /**
     * Set the moment
     */
    function _putRange(slots, ranges) {
      if (ranges) {
        ranges.forEach(function (range) {
          slots.push(range);
        });
      }
    }

    function _sortByDate(a,b){
      if(moment(a.start).isBefore(moment(b.start))){
        return -1;
      }
      if(moment(a.start).isAfter(moment(b.start))){
        return 1;
      }
      return 0;
    }

    /**
     * Slots are read in database and a table is build to be able to display a nice planning
     */
    function computeSlots(eventDate, slotInDatabase, rooms) {
      var slotsByRoom = slotInDatabase;
      if (rooms) {
        rooms.forEach(function (room) {
          //We see if slots exists for this room
          if (!slotsByRoom[room.key]) {
            slotsByRoom[room.key] = computeRange(moment(eventDate), {hour: 8, minute: 0}, {hour: 19, minute: 0});
          }
          else {
            var slots = [], elt, time;
            var lastTime = {
              hour: 8,
              minute: 0
            };
            slotsByRoom[room.key] = slotsByRoom[room.key].sort(_sortByDate);

            for (var i in slotsByRoom[room.key]) {
              elt = slotsByRoom[room.key][i];

              if (filterPlanningByDate(elt, eventDate)) {
                time = {
                  hour: moment(elt.start).get('hour'),
                  minute: moment(elt.start).get('minute')
                };

                if (time.hour !== lastTime.hour || time.minute !== lastTime.minute) {
                  _putRange(slots, computeRange(moment(elt.start), lastTime, time));
                }

                elt.duration = moment.duration(moment(elt.end).diff(moment(elt.start))).as('minutes');
                slots.push(elt);

                lastTime = {
                  hour: moment(elt.end).get('hour'),
                  minute: moment(elt.end).get('minute')
                };
              }
            }

            //For the last one we have to verify the last range
            if (lastTime.hour < 19) {
              _putRange(slots, computeRange(moment(eventDate), lastTime, {hour: 19, minute: 0}));
            }
            slotsByRoom[room.key] = slots;
          }
        });
      }
      return $q.when(slotsByRoom);
    }

    function _sessionExist(slots, session) {
      return slots.filter(function (slot) {
          return (slot.session && slot.session.id === session.idSession);
        }).length > 0;
    }

    /**
     * Extract the sessions not affected in a planning slot
     */
    function extractSessionToAffect(slotInDatabase, sessions) {
      if (!sessions || !slotInDatabase) {
        return undefined;
      }
      return sessions.filter(function (session) {
        var notExist = true;
        Object.keys(slotInDatabase).forEach(function (key) {
          if (_sessionExist(slotInDatabase[key], session) && notExist) {
            notExist = false;
          }
        });
        return notExist;
      });
    }

    function _getTimeSlot(date, hour, minute) {
      var momentDate = _setTime(date, {hour: hour, minute: minute});
      return {
        key: momentDate.format(DATE_FORMAT),
        label: momentDate.format('HH:mm')
      };
    }

    function getTimeSlots(date) {
      var momentDate = moment(date);
      var slots = [];

      for (var h = 8; h < 19; h++) {
        for (var min = 0; min < 6; min++) {
          slots.push(_getTimeSlot(momentDate, h, min * 10));
        }
      }

      return slots;
    }

    function filterPlanningByDate(slot, date) {
      return moment(slot.start).format('YYYYMMDD') === moment(date).format('YYYYMMDD');
    }

    function _dateInSlotPeriod(date, slot){
      if(!date){
        return false;
      }

      return moment(slot.start).isBefore(moment(date)) &&
        moment(slot.end).isAfter(moment(date));
    }

    function verifySlot(slot, slotsInRoom) {
      //Check validity
      if (!slot.idSession && !slot.label) {
        return 'SESSION_REQUIRED';
      }
      var concurrent = slotsInRoom.filter(function(s){
        return _dateInSlotPeriod(s.start, slot, false) || _dateInSlotPeriod(s.end, slot, true);
      }).length>0;
      if(concurrent){
        return 'SLOT_CONCURRENT';
      }
      return undefined;
    }

    return {
      filterPlanningByDate: filterPlanningByDate,
      getEventOutOfSession: getEventOutOfSession,
      getRoom: getRoom,
      getSlots: getSlots,
      getTransversalSlots: getTransversalSlots,
      getTimeSlots: getTimeSlots,
      computeSlots: computeSlots,
      computeRange: computeRange,
      convertDate: convertDate,
      extractSessionToAffect: extractSessionToAffect,
      verifySlot: verifySlot
    };
  });
})();