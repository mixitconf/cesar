(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function () {
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
          nbitems : 15
          };

        $scope.displayItem = function (index){
          var min = $scope.pagination.current * $scope.pagination.nbitems - $scope.pagination.nbitems;
          var max = $scope.pagination.current * $scope.pagination.nbitems - 1;
          return index>=min && index<=max;
        }

        $scope.$watch('sessions', function(newvalue){
          $scope.pagination.nbtotal = newvalue ? newvalue.length : 0;
          $scope.pagination.pages = parseInt($scope.pagination.nbtotal/$scope.pagination.nbitems);
        });
      }
    };
  });
})();
