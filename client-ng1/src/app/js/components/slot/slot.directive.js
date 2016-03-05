(function () {

  'use strict';

  /**
   * Creates a new button with a spinner displayed when a request is launched to the server (@see spinner.inteceptor.js)
   */
  angular.module('cesar-home').directive('cesarSlot', function () {
    'ngInject';

    return {
      templateUrl: 'js/components/slot/slot.directive.html',
      scope: {
        start:'=',
        end:'=',
        room:'='
      }
    };
  });
})();