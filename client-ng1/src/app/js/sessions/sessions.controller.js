(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, cesarSpinnerService, $state, $rootScope, $timeout, account) {
    'ngInject';

    var ctrl = this;
    var type = $state.current.data.type;
    var speakers;
    ctrl.userConnected = !!account;

    function _setSpeaker(response) {
      speakers = response.data;
      return MemberService.getAllSponsors($rootScope.cesar.current);
    }

    function _setSponsor(response) {
      ctrl.sponsors = response.data;
    }

    function _findSpeaker() {
      SessionService.findSessionsSpeakers(ctrl.sessions, speakers);
    }

    cesarSpinnerService.wait();
    if (type === 'talks') {
      //we load talks, workshop and keynotes
      SessionService.getAllByYear()
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAll('speaker', $rootScope.cesar.current);
        })
        .then(_setSpeaker)
        .then(_setSponsor)
        .then(_findSpeaker)
        .finally(function () {
          cesarSpinnerService.stopWaiting();
        });
    }
    else {
      SessionService.getAll(type)
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAllLigthningtalkSpeakers();
        })
        .then(_setSpeaker)
        .then(_setSponsor)
        .then(_findSpeaker)
        .finally(function () {
          cesarSpinnerService.stopWaiting();
        });
    }

  });
})();