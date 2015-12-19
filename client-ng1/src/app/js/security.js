(function () {

  'use strict';

  angular.module('cesar-security').constant('USER_ROLES', {
    all: '*',
    admin: 'ADMIN',
    speaker: 'SPEAKER',
    member: 'MEMBER'
  });


  angular.module('cesar-security').config(function ($httpProvider) {
    'ngInject';

    // alternatively, register the interceptor via an anonymous factory
    $httpProvider.interceptors.push(function ($q, $rootScope) {
      return {
        'responseError': function (response) {
          if ((response.status === 401 || response.status === 403) && !response.config.ignoreErrorRedirection) {
            var deferred = $q.defer();
            $rootScope.$emit('event:auth-loginRequired', response);
            return deferred.promise;
          }
          else if (response.status === 423 && !response.config.ignoreErrorRedirection) {
            //var deferred = $q.defer()
            $rootScope.$emit('event:$cesarError', {type : 'ACCOUNT_NOT_VALID'});
            //return deferred.promise;
          }
          // otherwise, default behaviour
          return $q.reject(response);
        }
      };
    });
  });
})();