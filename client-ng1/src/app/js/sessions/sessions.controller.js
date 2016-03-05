(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, cesarSpinnerService, $rootScope, $timeout,$q, account) {
    'ngInject';

    var ctrl = this;
    var speakers;
    ctrl.userConnected = !!account;

    function _setSpeaker(response) {
      speakers = response.data;
      return $q.when('');
    }

    function _setSponsor(response) {
      ctrl.sponsors = response.data;
    }

    function _findSpeaker() {
      SessionService.findSessionsSpeakers(ctrl.sessions, speakers);
    }

    cesarSpinnerService.wait();

    //we load talks, workshop and keynotes
    $q
      .all([
        SessionService.getAllByYear()
          .then(function (response) {
            ctrl.sessions = response.data;
            return MemberService.getAll('speaker', $rootScope.cesar.current);
          })
          .then(_setSpeaker),
        MemberService.getAll('sponsor', $rootScope.cesar.current)
          .then(_setSponsor)
      ])
      .then(_findSpeaker)
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

  });
})();