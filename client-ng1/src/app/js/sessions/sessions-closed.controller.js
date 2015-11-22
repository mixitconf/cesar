(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($rootScope, $state, SessionService) {
    'ngInject';

    var ctrl = this;
    var year = $state.current.data.year;

    ctrl.year = year;

    $rootScope.wait();
    SessionService.getAllByYear(year)
      .then(function (response) {
        ctrl.sessions = response.data;
      })
      .finally($rootScope.stopWaiting);
  });
})();