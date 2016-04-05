(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('PlanningTabCtrl', function ($q, $rootScope, $filter, $state, $stateParams, slots, transversalSlots, rooms,
                                                                           favorites, account, FavoriteService, PlanningService) {
    'ngInject';

    var ctrl = this;

    ctrl.dates = [$rootScope.cesar.day1, $rootScope.cesar.day2];
    ctrl.display = {
      amphi : true,
      salle: true,
      day1: true,
      day2: true,
      fr: true,
      en: true
    };

    ctrl.rooms = rooms;
    ctrl.userConnected = !!account;
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
        PlanningService.computeSlots(ctrl.dates[0], angular.copy(slots), ctrl.rooms).then(function (response) {
          ctrl.day1Slots = response;
        }),
        PlanningService.computeSlots(ctrl.dates[1], angular.copy(slots), ctrl.rooms).then(function (response) {
          ctrl.day2Slots = response;
        })
      ])
      .then(function () {
        ctrl.remainingSessions = PlanningService.extractSessionToAffect(ctrl.day2Slots, PlanningService.extractSessionToAffect(ctrl.day1Slots, ctrl.sessions));
      });


    ctrl.computeHeight = function(slot){
      if(slot.duration>60){
        return (slot.duration - 5)*2.0  + 'px';
      }
      return (slot.duration - 2)*2.0  + 'px';
    };

    ctrl.displayRoom = function (room) {
      switch(room.key){
        case 'Amphi1':
          return ctrl.display.amphi;
        case 'Amphi2':
          return ctrl.display.amphi;
        case 'Salle6':
        case 'Salle7':
          return ctrl.display.mezzanine;
      }
      return ctrl.display.salle;
    };

    //if (ctrl.userConnected) {
    //  ctrl.toggleFavorite = function(session){
    //    ctrl.errorMessage = undefined;
    //    FavoriteService
    //      .toggleFavorite(session)
    //      .then(function(){
    //        session.favorite = !!session.favorite ? false : true;
    //      })
    //      .catch(function () {
    //        ctrl.errorMessage = 'UNDEFINED';
    //      });
    //  };
    //}
  });
})();
