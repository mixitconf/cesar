(function () {

  'use strict';

  angular.module('cesar-utils').factory('cesarErrorInterceptor', function ($rootScope, $q) {

    function isFunctionalError(response) {
      return response.headers('Content-Type') &&
          response.headers('Content-Type').indexOf('application/json') === 0 &&
          angular.isDefined(response.data.message);
    }

    return {
      responseError: function(response){
        if (!isFunctionalError(response)) {
          $rootScope.$emit('$cesarError', response);
        }
        return $q.reject(response);
      }
    };
  });

  angular.module('cesar-utils').config(function($httpProvider) {
    $httpProvider.interceptors.push('cesarErrorInterceptor');
  });
})();