(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, Util, $state, $rootScope) {
    'ngInject';

    var ctrl = this;
    var type = $state.current.data.type;

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

    $rootScope.wait();
    if (type === 'talks') {
      //we load talks, workshop and keynotes
      SessionService.getAll('talk')
        .then(function (response) {
          ctrl.sessions = response.data;
          return SessionService.getAll('workshop');
        })
        .then(function (response) {
          Array.prototype.push.apply(ctrl.sessions, response.data);
          return SessionService.getAll('keynote');
        })
        .then(function (response) {
          Array.prototype.push.apply(ctrl.sessions, response.data);
          return MemberService.getAll('speaker');
        })
        .then(findSpeaker)
        .finally($rootScope.stopWaiting);
    }
    else {
      SessionService.getAll(type)
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAllLigthningtalkSpeakers();
        })
        .then(findSpeaker)
        .finally($rootScope.stopWaiting);
    }

  });
})();