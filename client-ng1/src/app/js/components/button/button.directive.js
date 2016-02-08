(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   */
  angular.module('cesar-home').directive('cesarButton', function () {
    'ngInject';

    return {
      templateUrl: 'js/components/button/button.directive.html',
      scope: {
        disabled: '=',
        onClick: '&',
        spinner: '=',
        googleIcon: '@',
        default: '@',
        text:'@'
      },
      controller: function($scope){
        $scope.type = $scope.default ? 'submit' : 'button';
      }
    };
  });
})();