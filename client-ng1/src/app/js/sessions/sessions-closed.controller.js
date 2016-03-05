(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($state, SessionService, MemberService, cesarSpinnerService) {
    'ngInject';

    var ctrl = this;
    var year = $state.current.data.year;
    var speakers;
    ctrl.year = year;

    cesarSpinnerService.wait();

    MemberService.getAll('speaker', year)
      .then(function (response) {
        speakers = response.data;
        return SessionService.getAllByYear(year);
      })
      .then(function (response) {
        ctrl.sessions = response.data;
        SessionService.findSessionsSpeakers(ctrl.sessions, speakers);
        return MemberService.getAllSponsors(year);
      })
      .then(function (response) {
        ctrl.sponsors = response.data;
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

  });
})();