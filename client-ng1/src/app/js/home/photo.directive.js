(function () {

  'use strict';

  angular.module('cesar-home').directive('cesarPhoto', function () {
    return {
      templateUrl: 'js/home/photo.directive.html',
      scope: {
        url : '@'
      }
    };
  });
})();