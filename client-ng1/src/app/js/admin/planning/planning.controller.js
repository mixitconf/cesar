(function () {

  'use strict';

  angular.module('cesar-planning').controller('AdminPlanningCtrl', function ($rootScope, $q, account, SessionService, PlanningService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var slots;

    if (!account) {
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    cesarSpinnerService.wait();

    $q.all([
        PlanningService.getRoom().then(function (response) {
          ctrl.rooms = response.data;
        }),
        PlanningService.getSlots(2015).then(function (response) {
          slots = response.data;
        }),
        SessionService.getAllByYear(2015).then(function (response) {
          ctrl.sessions = response.data;
        })
      ])
      .then(function () {
        PlanningService.computeSlots(slots, ctrl.rooms).then(function (response) {
          ctrl.slots = response;
          ctrl.timeslots = PlanningService.computeRange();
        });
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });
  });
})();
