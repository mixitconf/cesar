(function () {

  'use strict';

  angular.module('cesar-home').directive('cesarNbCarac', function () {
    'ngInject';

    return {
      templateUrl: 'js/components/nbcarac/nbcarac.directive.html',
      scope: {
        nbcarac : '@',
        text : '=',
        markdown : '@',
        field : '='
      }
    };
  });
})();