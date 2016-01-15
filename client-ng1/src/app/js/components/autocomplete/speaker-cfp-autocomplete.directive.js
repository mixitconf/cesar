(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   * TODO refactoring to mutualize code between alla autocomplete directives
   */
  angular.module('cesar-home').directive('cesarSpeakerAutocomplete', function ($http, $timeout) {
    'ngInject';

    var speakers, opened;

    $http.get('app/cfp/proposal/speakers').then(function(response){
      speakers = response.data;
    });

    return {
      templateUrl: 'js/components/autocomplete/speaker-cfp-autocomplete.directive.html',
      scope: {
        addSpeaker: '&',
        limit : '@',
        value :'=',
        legend: '@'
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
          $scope.addSpeaker({value:selected});
          $scope.close();
          $scope.reset();
        };

        $scope.reset = function(){
          delete $scope.value;
        };

        $scope.autocomplete = function(prefix){
          if(prefix && opened){
            var q = prefix.toUpperCase().trim();

            $scope.subselect =  speakers.filter(function(elt){
              return elt.key.toUpperCase().indexOf(q) > 0;
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