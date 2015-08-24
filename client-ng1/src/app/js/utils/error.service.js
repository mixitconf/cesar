(function () {

  'use strict';

  angular.module('cesar-services').service('cesarErrorService', function ($rootScope) {

    function throwError(response) {
      $rootScope.$emit('$cesarError', response);
    }

    return {
      throwError: throwError
    };
  });
})();