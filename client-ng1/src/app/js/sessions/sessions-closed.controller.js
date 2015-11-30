(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($rootScope, $state, SessionService, MemberService) {
    'ngInject';

    var ctrl = this;
    var year = $state.current.data.year;

    ctrl.year = year;

    $rootScope.wait();
    SessionService.getAllByYear(year)
      .then(function (response) {
        ctrl.sessions = response.data;
        return MemberService.getAll('sponsor', year);
      })
      .then(function (response) {
        ctrl.sponsors = response.data;
      })
      .finally($rootScope.stopWaiting);

  });
})();