(function () {
  'use strict';

  angular.module('cesar-security').directive('cesarConfirmPassword', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      scope: {
        password: '='
      },
      link: function (scope, element, attributes, ngModelController) {
        ngModelController.$validators.validPassword = function (modelValue, viewValue) {
          var value = modelValue || viewValue;
          return value === scope.password;
        };
      }
    };
  });


})();
