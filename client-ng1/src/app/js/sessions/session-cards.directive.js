(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function ($filter) {
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
        $scope.pagination = {
          current : 1,
          nbitems : 10
          };

        $scope.displayItem = function (index){
          var min = $scope.pagination.current * $scope.pagination.nbitems - $scope.pagination.nbitems;
          var max = $scope.pagination.current * $scope.pagination.nbitems - 1;
          return index>=min && index<=max;
        };

        $scope.filter= function(){
          var sessions =  $filter('filter')($scope.sessions, $scope.search);
          $scope.pagination.nbtotal = sessions ? sessions.length : 0;
          $scope.pagination.pages = parseInt($scope.pagination.nbtotal/$scope.pagination.nbitems);
          return sessions;
        };

      }
    };
  });
})();
