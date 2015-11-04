(function () {

  'use strict';

  angular.module('cesar-home').directive('cesarHomeIntro', function () {
    'ngInject';

    return {
      templateUrl: 'js/home/intro.directive.html',
      scope: {}
    };
  });
})();