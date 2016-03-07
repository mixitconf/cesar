(function () {

  'use strict';
  /*global componentHandler */

  angular.module('cesar-cfp').controller('AdminCfpTalkCtrl', function ($http, $state, $stateParams, $timeout, account) {
    'ngInject';

    var ctrl = this;

    if ($stateParams.id) {
      $http
        .get('app/cfp/proposal/' + $stateParams.id)
        .then(function (response) {
          ctrl.proposal = response.data;
          ctrl.check();
        });

      $http.get('app/cfp/proposal/votes').then(
          function(response){
            ctrl.votesMappedByProposalId = {};
            angular.forEach(response.data, function(data) {
              ctrl.votesMappedByProposalId[data.proposalId] = { vote:data.voteValue, comment: data.voteComment };
            });
          }
      );
    }

    ctrl.account = account;

    var i8nKeys = {
      categories: 'view.cfp.category.',
      status: 'view.cfp.status.',
      formats: 'view.cfp.format.',
      types: 'view.cfp.typeSession.',
      levels: 'view.cfp.level.',
      maxAttendees: 'view.cfp.nbattendees.'
    };

    $http.get('app/cfp/param').then(function (response) {
      response.data.forEach(function (elt) {
        ctrl[elt.key] = [];
        elt.value.forEach(function (param) {
          ctrl[elt.key][param] = i8nKeys[elt.key] + param;
        });
      });
    });

    ctrl.check = function () {
      if(ctrl.proposal.id){
        delete ctrl.warningMessage;
        delete ctrl.confirm;

        $http
          .post('app/cfp/proposal/check', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function (response) {
            ctrl.warningMessage = response.data;
            refresh();
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      }
    };

    ctrl.goback = function () {
      $state.go('cfp');
    };

    ctrl.vote = function (voteValue) {
      var data = {
        proposalId: ctrl.proposal.id,
        voteValue: voteValue
      };
      $http.post('app/cfp/proposal/vote', data);
      if ( !ctrl.votesMappedByProposalId[ctrl.proposal.id] ) {
        ctrl.votesMappedByProposalId[ctrl.proposal.id] = {};
      }
      ctrl.votesMappedByProposalId[ctrl.proposal.id].vote = voteValue;
    };

    ctrl.saveVoteComment = function () {
      var data = {
        proposalId: ctrl.proposal.id,
        voteComment: ctrl.votesMappedByProposalId[ctrl.proposal.id].comment
      };
      $http.post('app/cfp/proposal/vote-comment', data);
    };

    function refresh() {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      }, 1000);
    }

  });
})();
