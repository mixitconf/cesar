(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalksCtrl', function (SessionService, MemberService, cesarSpinnerService, paginationService,  $http, $q, $state, account) {
    'ngInject';

    var ctrl = this;
    var mylighnings, myvotes;
    ctrl.userConnected = !!account;
    ctrl.editionMode = false;
    ctrl.pagination = paginationService.createPagination('-positiveVotes');

    function _getVotes(){
      if(!ctrl.userConnected){
        return $q.when({data : []});
      }
      return $http.get('app/session/lightnings/votes');
    }

    function _getMyLigthning(){
      if(!ctrl.userConnected){
        return $q.when({data : []});
      }
      return $http.get('app/session/mylightnings');
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
        _getVotes()
          .then(function (response) {
            myvotes = response.data;
          }),
        _getMyLigthning()
          .then(function (response) {
            mylighnings = response.data;
          })
      ])
      .then(function(){
        ctrl.sessions.forEach(function(session){
          //I need to know if I can modify an abstract
          if(mylighnings.some(function(mylt){
            return mylt.idSession === session.idSession;
          })){
            session.edition = true;
          }

          var votes = myvotes.filter(function(vote){
            return vote.idSession === session.idSession;
          });

          if(votes && votes.length>0){
            session.myvote = votes[0].value ? 2 : -2;
          }
        });
      })
      .finally(function () {
        cesarSpinnerService.stopWaiting();
      });

    ctrl.zoom = function(session){
      if(session.edition){
        $state.go('lightning', {id : session.idSession});
      }
      else{
        $state.go('lightning', {id : session.idSession});
      }

    };
  });
})();