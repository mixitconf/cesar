(function () {

  'use strict';
  /*global moment */
  /*global confirm */

  angular.module('cesar-planning').controller('AdminPlanningCtrl', function ($rootScope, $q, $http, account, SessionService, PlanningService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var slots, transversalSlots;
    var year=$rootScope.cesar.current;

    ctrl.dates = [$rootScope.cesar.day1, $rootScope.cesar.day2];
    ctrl.display = {
      amphi : true,
      salle: true,
      day1: true,
      day2: true,
      fr: true,
      en: true
    };

    if (!account) {
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    function _computeSlots(){
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
    }

    function _refresh(){
      cesarSpinnerService.wait();

      $q.all([
        PlanningService.getSlots(year).then(function (response) {
          slots = response.data;
        }),
        PlanningService.getTransversalSlots(year).then(function (response) {
          transversalSlots = response.data;
        })
      ])
        .then(_computeSlots)
        .finally(function () {
          cesarSpinnerService.stopWaiting();
        });
    }

    function _check(){
      if(ctrl.slot.session && !ctrl.slot.room){
        ctrl.errorMessage = 'ROOM_REQUIRED';
        return false;
      }
      return true;
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
      if(idSlot && confirm('Sûr de vouloir supprimer ce slot ?')){
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
      if(!_check()){
        return;
      }
      var slotToSave = {
        id : ctrl.slot.id,
        start : PlanningService.convertDate(ctrl.slot.start),
        end : PlanningService.convertDate(ctrl.slot.end),
        label : ctrl.slot.label,
        idSession : ctrl.slot.session ? ctrl.slot.session.idSession : undefined
      };

      var rooms;
      if(ctrl.slot.room){
        slotToSave.room = ctrl.slot.room.key;
        rooms = [ctrl.slot.room.key];
      }
      else {
        rooms = ctrl.rooms.map(function(elt){
          return elt.key;
        });
      }

      var used;
      for(var index in rooms){
        var room = rooms[index];
        used = PlanningService.verifySlot(slotToSave, ctrl.day1Slots[room]);
        if(!used){
          used = PlanningService.verifySlot(slotToSave, ctrl.day2Slots[room]);
        }
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
      ctrl.timeslotsAvailable = PlanningService.getTimeSlots(ctrl.slot.day);
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
        PlanningService.getTransversalSlots(year).then(function (response) {
          transversalSlots = response.data;
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
