(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, cesarSpinnerService, $q, $state, $rootScope, $timeout, account) {
    'ngInject';

    var ctrl = this;
    var type = $state.current.data.type;
    var speakers;
    ctrl.userConnected = !!account;

    function _setSpeaker(response) {
      speakers = response.data;
      return $q.when('');
    }

    function _findSpeaker() {
      SessionService.findSessionsSpeakers(ctrl.sessions, speakers);
    }

    cesarSpinnerService.wait();
    if (type === 'talks') {
      //we load talks, workshop and keynotes
      $q
        .all([
          SessionService.getAllByYear()
            .then(function (response) {
              ctrl.sessions = response.data;
              return MemberService.getAll('speaker');
            })
            .then(_setSpeaker),
          MemberService.getAll('sponsor', $rootScope.cesar.current).then(function (response) {
            ctrl.sponsors = response.data;
          })
        ])
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
        .then(_findSpeaker)
        .finally(function () {
          cesarSpinnerService.stopWaiting();
        });
    }

  });
})();