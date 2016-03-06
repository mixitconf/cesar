(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalksCtrl', function (SessionService, MemberService, cesarSpinnerService, paginationService,  $timeout, $q, $state, account) {
    'ngInject';

    var ctrl = this;
    ctrl.userConnected = !!account;
    ctrl.editionMode = false;

    ctrl.pagination = paginationService.createPagination('-positiveVotes');

    cesarSpinnerService.wait();
    $q
      .all([
        SessionService.getAll('lightningtalks')
          .then(function (response) {
            ctrl.sessions = response.data;
            ctrl.pagination.set(ctrl.sessions);
            return MemberService.getAllLigthningtalkSpeakers();
          })
          .then(function (response) {
            SessionService.findSessionsSpeakers(ctrl.sessions, response.data);
          }),
        MemberService.getAll('sponsor')
          .then(function (response) {
            ctrl.sponsors = response.data;
          })
      ])
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

    //TODO recup myvotes and myLT
    ctrl.zoom = function(session){
      console.log(session)
      //if(ctrl.userConnected)
      //$state
      //ui-sref="lightning({id : session.idSession})"
    }
  });
})();