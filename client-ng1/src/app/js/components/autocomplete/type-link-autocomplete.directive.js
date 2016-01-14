(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   * TODO refactoring to mutualize code between alla autocomplete directives
   */
  angular.module('cesar-home').directive('cesarTypeLinkAutocomplete', function ($timeout) {
    'ngInject';

    var opened;
    var types= [ 'Blog', 'Twitter', 'Google+', 'LinkedIn', 'Site web', 'Viadeo'];

    return {
      templateUrl: 'js/components/autocomplete/type-link-autocomplete.directive.html',
      scope: {
        value :'=',
        typeId : '@'
      },
      controller: function($scope){
        $scope.open = function(){
          opened = true;
        };

        $scope.close = function(){
          $timeout(function(){
            opened = false;
            $scope.subselect = undefined;
          }, 200);
        };

        $scope.add = function(selected){
          $scope.value = selected;
          $scope.close();
        };


        $scope.autocomplete = function(prefix){
          if(opened){
            var q = prefix ? prefix.toUpperCase().trim(): '';

            $scope.subselect =  types.filter(function(elt){
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