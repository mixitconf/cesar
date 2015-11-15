(function () {

  'use strict';

  /**
   * This interceptor is used to display a spinner when a request is executed
   */
  angular.module('cesar-utils').factory('cesarSpinnerInterceptor', function ($rootScope) {
    'ngInject';

    return {
      request: function(config) {
        $rootScope.spinner = 'on';
        return config;
      },

      requestError: function(rejection) {
        $rootScope.spinner = 'off';
        return $q.reject(rejection);
      },

      response: function(response) {
        $rootScope.spinner = 'off';
        return response;
      },

      responseError: function(rejection) {
        $rootScope.spinner = 'off';
        return $q.reject(rejection);
      }
    };
  });

})();