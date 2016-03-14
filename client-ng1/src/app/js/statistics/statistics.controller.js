(function () {

  'use strict';

  angular.module('cesar-account').controller('StatisticsCtrl', function ($http, $q, $rootScope, account) {
    'ngInject';

    var ctrl = this;
    ctrl.stats = {};

    if (!account) {
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    //Recuperate count of submitted session
    $q.all([
      $http.get('/app/cfp/vote/nbsessions/SUBMITTED')
        .then(function (response) {
          ctrl.stats.submitted = response.data;
          return $http.get('/app/cfp/vote/nbsessions/ACCEPTED');
        })
        .then(function (response) {
          ctrl.stats.accepted = response.data;
          return $http.get('/app/cfp/vote/nbsessions/REJECTED');
        })
        .then(function (response) {
          ctrl.stats.rejected = response.data;
        }),
      //Recuperate stats by user
      $http.get('/app/cfp/vote/summary')
        .then(function (response) {
          ctrl.stats.dones = response.data;
        }),
      //Recuperate all data for each proposal
      $http.get('/app/cfp/vote')
        .then(function (response) {
          ctrl.stats.proposals = response.data;
          ctrl.stats.proposals.forEach(function(elt){
            if(elt.votes && elt.votes.length>0){
              elt.totalVotes = elt
                .votes
                .map(function(e){
                  return e.voteValue;
                })
                .reduce(function(a, b) {
                  return a + b;
                });
            }
            else{
              elt.totalVotes = -9999;
            }
          });
        })
    ]);

    function _changeState(proposal, state){
      $http
        .put('app/cfp/proposal/' + proposal.id + '/state/' + state)
        .then(function () {
          proposal.status = state;
        })
        .catch(function () {
          ctrl.errorMessage = 'UNDEFINED';
        });
    }

    ctrl.accept = function (proposal) {
      _changeState(proposal, 'ACCEPTED');
    };

    ctrl.submit = function (proposal) {
      _changeState(proposal, 'SUBMITTED');
    };

    ctrl.reject = function (proposal) {
      _changeState(proposal, 'REJECTED');
    };

  });

})();