(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function (shuffleService, FavoriteService) {
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
        nbItems : '@',
        favorites : '=',
        connected : '='
      },
      controller: function($scope){
        if($scope.connected && $scope.favorites){
          FavoriteService.markFavorites($scope.sessions, $scope.favorites);

          $scope.toggleFavorite = function(session){
            FavoriteService
              .toggleFavorite(session)
              .then(function(){
                session.favorite = !!session.favorite ? false : true;
              })
              .catch(function (response) {
                console.error(response);
              });
          };
        }

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
