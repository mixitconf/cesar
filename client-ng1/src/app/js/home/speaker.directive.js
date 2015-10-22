(function () {

  'use strict';

  angular.module('cesar-home').directive('cesarHomeSpeaker', function () {
    'ngInject';

    return {
      templateUrl: 'js/home/speaker.directive.html',
      scope: {}
    };
  });
})();