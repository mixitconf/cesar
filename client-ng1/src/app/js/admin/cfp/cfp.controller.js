(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').controller('AdminCfpCtrl', function ($rootScope, $http, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }


    ctrl.refresh = function(){
      $http.get('app/cfp/proposal').then(
        function(response){
          ctrl.proposals = response.data;
        }
      );
      $http.get('app/cfp/proposal/votes').then(
          function(response){
            ctrl.votesMappedByProposalId = {};
            angular.forEach(response.data, function(data) {
              ctrl.votesMappedByProposalId[data.proposal.id] = data.voteValue;
            });
          }
      );
    };

    ctrl.voteVeto = function(proposalId){
      _vote(proposalId, 'VETO');
    };

    ctrl.voteBad = function(proposalId){
      _vote(proposalId, 'BAD');
    };

    ctrl.voteGood = function(proposalId){
      _vote(proposalId, 'GOOD');
    };

    ctrl.voteNeedIt = function(proposalId){
      console.log(proposalId);
      _vote(proposalId, 'NEED_IT');
    };

    function _vote(proposalId, voteValue) {
      var data = {
        proposalId: proposalId,
        voteValue: voteValue
      };
      $http.post('app/cfp/proposal/vote', data);
      ctrl.votesMappedByProposalId[proposalId] = voteValue;
    }

    ctrl.refresh();
  });
})();
