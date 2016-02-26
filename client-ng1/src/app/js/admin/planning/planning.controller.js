(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('AdminPlanningCtrl', function ($rootScope, $q, $http, account, SessionService, PlanningService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var slots;
    //We will work with 2016 after the CFP
    var year=2015;

    ctrl.dates = [$rootScope.cesar.day1, $rootScope.cesar.day2];
    ctrl.display = {
      amphi : true,
      salle: true,
      day1: true,
      day2: true
    };

    if (!account) {
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    function _computeSlots(){
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
    }

    function _refresh(){
      cesarSpinnerService.wait();

      PlanningService.getSlots(year).then(function (response) {
          slots = response.data;
        })
        .then(_computeSlots)
        .finally(function () {
          cesarSpinnerService.stopWaiting();
        });
    }

    ctrl.displayRoom = function (room) {
      switch(room.key){
        case 'Amphi1':
          return ctrl.display.amphi;
        case 'Amphi2':
          return ctrl.display.amphi;
        case 'Salle7':
          return ctrl.display.mezzanine;
      }
      return ctrl.display.salle;
    };

    ctrl.deleteSlot = function(idSlot){
      if(idSlot && confirm("Sûr de vouloir supprimer ce slot ?")){
        $http.delete('/app/planning/' + idSlot, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            _refresh();
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      }
    };

    ctrl.saveSlot = function(){
      delete ctrl.errorMessage;

      var slotToSave = {
        id : ctrl.slot.id,
        start : PlanningService.convertDate(ctrl.slot.start),
        end : PlanningService.convertDate(ctrl.slot.end),
        room : ctrl.slot.room.key,
        label : ctrl.slot.label,
        idSession : ctrl.slot.session ? ctrl.slot.session.idSession : undefined
      };

      var used = PlanningService.verifySlot(slotToSave, ctrl.day1Slots[ctrl.slot.room.key]);
      if(!used){
        used = PlanningService.verifySlot(slotToSave, ctrl.day2Slots[ctrl.slot.room.key]);
      }

      if(used){
        ctrl.errorMessage = used;
      }
      else{
        $http.post('/app/planning', slotToSave, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            _refresh();
          })
          .catch(function (response) {
            ctrl.errorMessage = response.data.type ? response.data.type : 'UNDEFINED';
          });
      }
    };

    ctrl.changeDate = function(){
      ctrl.timeslotsAvailable = PlanningService.getTimeSlots(ctrl.slot.day)
    };

    ctrl.reinit = function(){
      ctrl.slot = {
        day : $rootScope.cesar.day1
      };
      ctrl.changeDate();
    };



    cesarSpinnerService.wait();
    ctrl.timeslots = PlanningService.computeRange(moment(ctrl.dates[0]));
    ctrl.eventsOutOfSessions = PlanningService.getEventOutOfSession();

    $q.all([
        PlanningService.getRoom().then(function (response) {
          ctrl.rooms = response.data;
        }),
        PlanningService.getSlots(year).then(function (response) {
          slots = response.data;
        }),
        SessionService.getAllByYear(year).then(function (response) {
          ctrl.sessions = response.data;
        })
      ])
      .then(_computeSlots)
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });


    ctrl.reinit();
  });
})();
