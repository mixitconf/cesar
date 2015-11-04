(function () {

  'use strict';

  angular.module('cesar-home').directive('cesarHomeInfo', function () {
    'ngInject';

    return {
      templateUrl: 'js/home/info.directive.html',
      scope: {}
    };
  });
})();