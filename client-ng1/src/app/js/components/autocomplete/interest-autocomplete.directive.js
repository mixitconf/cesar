(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   */
  angular.module('cesar-home').directive('cesarInterestAutocomplete', function ($http, $timeout) {
    'ngInject';

    var interests, opened;

    $http.get('api/interest').then(function(response){
      interests = response.data;
    });

    return {
      templateUrl: 'js/components/autocomplete/interest-autocomplete.directive.html',
      scope: {
        addInterest: '&',
        limit : '@',
        value :'='
      },
      controller: function($scope){
        $scope.open = function(){
          opened = true;
        };

        $scope.close = function(){
          $timeout(function(){
            opened = false;
            $scope.subselect = undefined;
          }, 400);
        };

        $scope.add = function(selected){
          $scope.value = selected;
          $scope.addInterest({value:selected});
          $scope.close();
          $scope.reset();
        };

        $scope.reset = function(){
          delete $scope.value;
        };

        $scope.autocomplete = function(prefix){
          if(prefix && opened){
            var q = prefix.toUpperCase().trim();

            $scope.subselect =  interests.filter(function(elt){
              return elt.toUpperCase().indexOf(q) === 0;
            }).slice(0, $scope.limit ? $scope.limit : 5);
          }
          else{
            $scope.close();
          }
        };
      }
    };
  });
})();