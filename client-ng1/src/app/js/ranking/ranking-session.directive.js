(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarRankingSession', function () {
    'ngInject';

    return {
      templateUrl: 'js/ranking/ranking-session.directive.html',
      scope: {
        votes: '=',
        typeSession:'@',
        isDisplayByPercent: '=',
        nbPositiveVotes:'='
      },
      controller: function($scope){
        $scope.computeRatioForPositiveVote = function(session, type){
          return  Math.round(session.positiveVotes / $scope.nbPositiveVotes[type] * 100);
        };

      }
    };
  });
})();
