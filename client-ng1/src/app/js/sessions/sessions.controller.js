(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, Util, $state) {
    var ctrl = this;
    var type = $state.current.data.type;

    function findSpeaker(response) {
      ctrl.sessions.forEach(function (session) {
        var speakers = Array.isArray(session._links.speaker) ? session._links.speaker : [session._links.speaker];
        session.speakers = response.data.filter(function (speaker) {
          var found = speakers.filter(function (s) {
            return Util.extractId(s.href) === (speaker.idMember+'');
          });
          return found.length > 0;
        });
      });
    }

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
        .then(findSpeaker);
    }
    else {
      SessionService.getAll(type)
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAll('speaker');
        })
        .then(findSpeaker);
    }

  });
})();