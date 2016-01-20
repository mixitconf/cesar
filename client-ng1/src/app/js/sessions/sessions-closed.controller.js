(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($state, SessionService, MemberService, $q) {
    'ngInject';

    var ctrl = this;
    var year = $state.current.data.year;

    ctrl.year = year;

    $q.all([
      SessionService.getAllByYear(year).then(function (response) {
        ctrl.sessions = response.data;
      }),
      MemberService.getAll('sponsor', year).then(function (response) {
        ctrl.sponsors = response.data;
      })
    ]);

  });
})();