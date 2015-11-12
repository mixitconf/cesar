(function () {

  'use strict';

  angular.module('cesar-security').directive('cesarLogin', function ($http, $rootScope, $q) {

    'ngInject';

    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attributes, ngModelController) {

        ngModelController.$asyncValidators.uniqueLogin = function (modelValue, viewValue) {
          var deferred = $q.defer();

          if (!viewValue) {
            deferred.resolve(true);
          }
          else {
            // Lookup user by username
            $http.get('/app/account/cesar/' + viewValue)
              .then(function (response) {
                if (response.data.value === null || response.data.value === 'null') {
                  deferred.resolve(true);
                }
                else {
                  //username exists, this means validation fails
                  deferred.reject('exists');
                }
              })
              .catch(function () {
                //username does not exist, therefore this validation passes
                deferred.resolve(true);
              });
          }
          return deferred.promise;
        };
      }
    };
  });


})();
