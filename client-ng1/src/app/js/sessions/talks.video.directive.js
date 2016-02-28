(function () {

  'use strict';
  angular.module('cesar-home').directive('cesarTalkVideo', function () {
    'ngInject';

    return {
      templateUrl: 'js/sessions/talks.video.directive.html',
      scope: {},
      bindToController: {
        year : '='
      },
      controllerAs: 'ctrl',
      controller : angular.noop
    };
  });
})();