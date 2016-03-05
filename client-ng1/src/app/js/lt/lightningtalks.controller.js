(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalksCtrl', function (SessionService, MemberService, cesarSpinnerService, $rootScope, $timeout,$q, account) {
    'ngInject';

    var ctrl = this;
    var speakers;
    ctrl.userConnected = !!account;

    cesarSpinnerService.wait();
    $q
      .all([
        SessionService.getAll('lightningtalks')
          .then(function (response) {
            ctrl.sessions = response.data;
            return MemberService.getAllLigthningtalkSpeakers();
          })
          .then(function (response) {
            speakers = response.data;
          }),
        MemberService.getAll('sponsor', $rootScope.cesar.current)
          .then(function (response) {
            ctrl.sponsors = response.data;
          })
      ])
      .then(function() {
        SessionService.findSessionsSpeakers(ctrl.sessions, speakers);
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

  });
})();