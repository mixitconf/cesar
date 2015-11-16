(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   */
  angular.module('cesar-home').directive('cesarButton', function () {
    'ngInject';

    return {
      templateUrl: 'js/button/button.directive.html',
      scope: {
        disabled: '=',
        onClick: '&',
        spinner: '=',
        googleIcon: '@'
      }
    };
  });
})();