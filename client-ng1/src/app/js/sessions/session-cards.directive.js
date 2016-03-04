(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function ($filter, paginationService) {
    'ngInject';

    return {
      templateUrl: 'js/sessions/session-cards.directive.html',
      scope: {
        sessions: '=',
        search: '=',
        display: '@',
        displayVotes: '@',
        order: '@',
        skipIcon : '@'
      },
      controller: function($scope){
        $scope.pagination = paginationService.createPagination();

        $scope.$watchCollection('sessions', function(sessions){
          $scope.pagination.set(sessions);
        });
      }
    };
  });
})();
