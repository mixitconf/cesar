(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, Util, cesarSpinnerService, $q, $state, $rootScope, $timeout, account) {
    'ngInject';

    var ctrl = this;
    var type = $state.current.data.type;
    ctrl.userConnected = !!account;

    function findSpeaker(response) {
      ctrl.sessions.forEach(function (session) {
        var links = Array.isArray(session.links) ? session.links : [session.links];
        session.speakers = response.data.filter(function (speaker) {
          var found = links.filter(function (s) {
            if(s.rel!=='speaker'){
              return false;
            }
            return Util.extractId(s.href) === (speaker.idMember+'');
          });
          return found.length > 0;
        });
      });
    }

    cesarSpinnerService.wait();
    if (type === 'talks') {
      //we load talks, workshop and keynotes

      $q.all([
        SessionService.getAllByYear()
          .then(function (response) {
            ctrl.sessions = response.data;
            return MemberService.getAll('speaker');
          })
          .then(findSpeaker),
        MemberService.getAll('sponsor', $rootScope.cesar.current).then(function (response) {
          ctrl.sponsors = response.data;
        })
      ])
      .finally(function(){
        cesarSpinnerService.stopWaiting();
      });
    }
    else {
      SessionService.getAll(type)
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAllLigthningtalkSpeakers();
        })
        .then(findSpeaker)
        .finally(function(){
          cesarSpinnerService.stopWaiting();
        });
    }

  });
})();