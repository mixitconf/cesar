(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').controller('AdminCfpCtrl', function ($rootScope, $filter, $http, $scope, account) {
    'ngInject';

    var ctrl = this;
    var proposals;
    ctrl.votedFilters = {
      ALL: 'ALL',
      VOTED: 'VOTED',
      NOT_VOTED:'NOT_VOTED'
    };
    ctrl.votedFilter = ctrl.votedFilters.ALL;

    ctrl.selectedStatuses = ['SUBMITTED'];

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    ctrl.refresh = function(){
      $http.get('app/cfp/proposal').then(
        function(response){
          proposals = response.data;
        }
      );
      $http.get('app/cfp/proposal/votes').then(
          function(response){
            ctrl.votesMappedByProposalId = {};
            angular.forEach(response.data, function(data) {
              ctrl.votesMappedByProposalId[data.proposalId] = { vote:data.voteValue, comment: data.voteComment };
            });
          }
      );
    };

    ctrl.vote = function(proposalId, voteValue) {
      var data = {
        proposalId: proposalId,
        voteValue: voteValue
      };
      $http.post('app/cfp/proposal/vote', data);
        ctrl.votesMappedByProposalId[proposalId].vote = voteValue;
    };

    ctrl.filter = function(){
      var proposalsFiltered =  $filter('filter')(proposals, $scope.search);
      proposalsFiltered =  $filter('orderBy')(proposalsFiltered, 'status');

      if ( proposalsFiltered ) {
        proposalsFiltered = proposalsFiltered.filter(function (elem) {
          return ctrl.selectedStatuses.indexOf(elem.status) !== -1;
        });
      }

      if ( proposalsFiltered ) {
        proposalsFiltered = proposalsFiltered.filter(function(elem) {
          if ( ctrl.votedFilter === ctrl.votedFilters.ALL ) {
            return true;
          } else if ( ctrl.votedFilter === ctrl.votedFilters.VOTED ) {
            return ctrl.isVoted(elem.id);
          } else {
            return !ctrl.isVoted(elem.id);
          }
        });
      }

      ctrl.pagination.nbtotal = proposalsFiltered ? proposalsFiltered.length : 0;
      ctrl.pagination.pages = Math.ceil(ctrl.pagination.nbtotal/ctrl.pagination.nbitems);
      return proposalsFiltered;
    };

    ctrl.pagination = {
      current: 1,
      nbitems: 10
    };

    ctrl.displayItem = function (index){
      var min = ctrl.pagination.current * ctrl.pagination.nbitems - ctrl.pagination.nbitems;
      var max = ctrl.pagination.current * ctrl.pagination.nbitems - 1;
      return index>=min && index<=max;
    };

    ctrl.proposalStatuses = function() {
      return ['ACCEPTED', 'REJECTED', 'VALID', 'CREATED', 'SUBMITTED'];
    };

    ctrl.toggleStatusFilter = function(proposalStatus) {
      var index = ctrl.selectedStatuses.indexOf(proposalStatus);
      if ( index !== -1) {
        ctrl.selectedStatuses.splice(index, 1);
      } else {
        ctrl.selectedStatuses.push(proposalStatus);
      }

    };

    ctrl.isVoted = function (proposalId) {
      return !!ctrl.votesMappedByProposalId[proposalId] && !!ctrl.votesMappedByProposalId[proposalId].vote;
    };

    ctrl.toggleVotedFilter = function(votedFilter) {
      ctrl.votedFilter = votedFilter;
    };

    ctrl.refresh();
  });
})();
