(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('PlanningTabCtrl', function ($q, $rootScope, slots, transversalSlots, rooms, PlanningService) {
    'ngInject';

    var ctrl = this;

    ctrl.dates = [$rootScope.cesar.day1, $rootScope.cesar.day2];
    ctrl.display = {
      amphi : true,
      salle: true,
      day1: true,
      day2: true
    };

    ctrl.rooms = rooms;
    ctrl.timeslots = PlanningService.computeRange(moment(ctrl.dates[0]));
    ctrl.eventsOutOfSessions = PlanningService.getEventOutOfSession();


    transversalSlots.forEach(function(elt){
      for(var index in ctrl.rooms){
        var room = ctrl.rooms[index];
        elt.room = room;
        if(slots[room.key]){
          slots[room.key].push(elt);
        }
        else{
          slots[room.key] = [elt];
        }
      }
    });

    $q.all([
        PlanningService.computeSlots(ctrl.dates[0], angular.copy(slots), rooms).then(function (response) {
          ctrl.day1Slots = response;
        }),
        PlanningService.computeSlots(ctrl.dates[1], angular.copy(slots), rooms).then(function (response) {
          ctrl.day2Slots = response;
        })
      ])
      .then(function () {
        ctrl.remainingSessions = PlanningService.extractSessionToAffect(ctrl.day2Slots, PlanningService.extractSessionToAffect(ctrl.day1Slots, ctrl.sessions));
      });

  });
})();
