(function () {

  'use strict';

  angular.module('cesar-utils').service('cesarErrorService', function ($rootScope) {

    function throwError(response) {
      $rootScope.$emit('$cesarError', response);
    }

    return {
      throwError: throwError
    };
  });
})();