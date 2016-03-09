(function () {

  'use strict';

  angular.module('cesar-account').controller('StatisticsCtrl', function ($http) {
    'ngInject';

    var ctrl = this;
    ctrl.stats = {};

    //Recuperate count of submitted session
    $http.get('/app/cfp/vote/nbsessions/SUBMITTED')
      .then(function (response) {
        ctrl.stats.submitted = response.data;
      });
    //Recuperate count of accepted session
    $http.get('/app/cfp/vote/nbsessions/ACCEPTED')
      .then(function (response) {
        ctrl.stats.accepted = response.data;
      });
    //Recuperate count of rejected session
    $http.get('/app/cfp/vote/nbsessions/REJECTED')
      .then(function (response) {
        ctrl.stats.rejected = response.data;
      });

    //Recuperate stats by user
    $http.get('/app/cfp/vote/summary')
      .then(function (response) {
        ctrl.stats.dones = response.data;
      });

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
      });
  });

})();