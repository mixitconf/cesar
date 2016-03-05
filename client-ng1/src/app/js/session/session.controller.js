(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionCtrl', function (session, $stateParams, $http, Util, MemberService, SessionService, account) {
    'ngInject';

    var ctrl = this;

    ctrl.session = session;
    ctrl.session.speakers = [];
    ctrl.type = $stateParams.type;
    ctrl.userConnected = !!account;

    var speakers = Array.isArray(session._links.speaker) ? session._links.speaker : [session._links.speaker];

    speakers.forEach(function (speaker) {
      MemberService.getById(Util.extractId(speaker.href)).then(function (response) {
        ctrl.session.speakers.push(response.data);
      });
    });

    function _refreshVote() {
      delete ctrl.myvote;
      $http.get('/app/vote/session/' + session.idSession).then(function (response) {
        if (response.data) {
          ctrl.myvote = response.data.value ? 2 : -2;
        }
      });
      SessionService.getById(session.idSession, true).then(function (response) {
        ctrl.session.votes = response.data.votes;
        ctrl.session.positiveVotes = response.data.positiveVotes;
      });
    }

    if (ctrl.userConnected) {
      //We need to know if user has voted
      _refreshVote();

      //Call server to read vote
      ctrl.vote = function (note) {
        var vote = {
          idSession: session.idSession,
          value: note > 0
        };

        $http
          .post('/app/vote/', vote)
          .then(function () {
            _refreshVote();
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      };
    }

  });
})();