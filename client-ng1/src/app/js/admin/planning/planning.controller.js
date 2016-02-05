(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('AdminPlanningCtrl', function ($rootScope, $q, account, SessionService, PlanningService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var slots;
    //We will work with 2016 after the CFP
    var year=2015;

    ctrl.dates = ['2016-04-21T08:00:00Z', '2016-04-22T08:00:00Z'];

    ctrl.display = {
      amphi : true,
      salle: true
    };

    if (!account) {
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    cesarSpinnerService.wait();

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
      .then(function () {
        PlanningService.computeSlots(ctrl.dates[0], slots, ctrl.rooms).then(function (response) {
          ctrl.slots = response;
          ctrl.remainingSessions = PlanningService.extractSessionToAffect(ctrl.slots, ctrl.sessions);
        });
        ctrl.timeslots = PlanningService.computeRange(moment(ctrl.dates[0]));
        ctrl.timeslotsAvailable = PlanningService.getTimeSlots(ctrl.dates[0]);
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

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

  });
})();
