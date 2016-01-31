(function () {

  'use strict';

  angular.module('cesar-planning').controller('AdminPlanningCtrl', function ($rootScope, $q, account, SessionService, PlanningService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    cesarSpinnerService.wait();
    $q.all([
        PlanningService.getRoom().then(function(response){
          ctrl.rooms = response.data;
        }),
        PlanningService.getSlots().then(function(response){
          ctrl.slots = response.data;
        }),
        SessionService.getAllByYear(2015).then(function (response) {
          ctrl.sessions = response.data;
        })
      ])
      .finally(function(){
        cesarSpinnerService.stopWaiting();
      });
  });
})();
