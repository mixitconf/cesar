(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalksCtrl', function (SessionService, MemberService, cesarSpinnerService, paginationService, $http, $q, $state, account, myvotes, mylighnings) {
    'ngInject';

    var ctrl = this;
    var loaded = false;
    ctrl.userConnected = !!account;
    ctrl.editionMode = false;
    ctrl.pagination = paginationService.createPagination('-positiveVotes');

    function _updateVote(session) {
      var votes = myvotes.filter(function (vote) {
        return vote.idSession === session.idSession;
      });

      if (votes && votes.length > 0) {
        session.myvote = votes[0].value ? 2 : -2;
      }
    }

    function _getVotes() {
      if (!ctrl.userConnected) {
        myvotes = [];
      }
      else {
        $http.get('app/session/lightnings/votes').then(function (response) {
          myvotes = response.data;
          ctrl.sessions.forEach(function (session) {
            _updateVote(session);
          });
        });
      }
    }


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
          }),

      ])
      .then(function () {
        ctrl.sessions.forEach(function (session) {
          //I need to know if I can modify an abstract
          if (mylighnings && mylighnings.some(function (mylt) {
              return mylt.idSession === session.idSession;
            })) {
            session.edition = true;
          }

          _updateVote(session);
        });
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
        //We need to used this awful hack to be sure that everything is in place when you use
        //a degraded mobile connection
        loaded = true;
      });

    ctrl.zoom = function (session) {
      if (loaded) {
        if (session.edition) {
          $state.go('lightning-edition', {id: session.idSession});
        }
        else {
          $state.go('lightning', {id: session.idSession});
        }
      }
    };

    if (ctrl.userConnected) {
      //Call server to read vote
      ctrl.vote = function (session, note) {
        var vote = {
          idSession: session.idSession,
          value: note > 0
        };

        $http
          .post('/app/vote/', vote)
          .then(function () {
            if (angular.isDefined(session.myvote)) {
              if (session.myvote < 0 && vote.value) {
                session.positiveVotes++;
              }
              if (session.myvote > 0 && !vote.value) {
                session.positiveVotes--;
              }
            }
            else {
              if (vote.value) {
                session.positiveVotes++;
              }
              else {
                session.positiveVotes--;
              }
            }
            _getVotes();
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      };
    }
  });
})();