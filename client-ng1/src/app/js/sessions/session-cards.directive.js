(function () {

  'use strict';

  angular.module('cesar-sessions').directive('cesarSessionCards', function () {
    return {
      templateUrl: 'js/sessions/session-cards.directive.html',
      scope: {
        sessions: '=',
        search: '=',
        display: '@',
        displayVotes: '@',
        order: '@',
        skipIcon : '@'
      }
    };
  });
})();
