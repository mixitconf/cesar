(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function (shuffleService) {
    'ngInject';

    return {
      templateUrl: 'js/sessions/session-cards.directive.html',
      scope: {
        sessions: '=',
        search: '=',
        year:'@',
        display: '@',
        order: '@',
        skipIcon : '@',
        nbItems : '@'
      },
      controller: function($scope){
        $scope.shuffle = shuffleService.createShuffle($scope.order);

        $scope.$watchCollection('sessions', function(sessions){
          $scope.shuffle.set(sessions);
          if($scope.nbItems){
            $scope.shuffle.setNbItems($scope.nbItems);
          }
        });
      }
    };
  });
})();
