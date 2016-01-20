(function () {

  'use strict';

  /**
   * This interceptor is used to display a spinner when a request is executed
   */
  angular.module('cesar-utils').factory('cesarSpinnerInterceptor', function ($q, cesarSpinnerService) {
    'ngInject';

    return {
      request: function(config) {
        cesarSpinnerService.activate();
        return config;
      },

      requestError: function(rejection) {
        cesarSpinnerService.desactivate();
        return $q.reject(rejection);
      },

      response: function(response) {
        cesarSpinnerService.desactivate();
        return response;
      },

      responseError: function(rejection) {
        cesarSpinnerService.desactivate();
        return $q.reject(rejection);
      }
    };
  });

})();