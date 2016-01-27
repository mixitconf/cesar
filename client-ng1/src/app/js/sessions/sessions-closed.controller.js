(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($state, SessionService, MemberService, $q, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var year = $state.current.data.year;

    ctrl.year = year;

    cesarSpinnerService.wait();
    $q.all([
      SessionService.getAllByYear(year).then(function (response) {
        ctrl.sessions = response.data;
      }),
      MemberService.getAll('sponsor', year).then(function (response) {
        ctrl.sponsors = response.data;
      })
    ])
    .finally(function(){
      cesarSpinnerService.stopWaiting();
    });

  });
})();