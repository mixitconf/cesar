(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarSessionCards', function () {
    return {
      templateUrl: 'js/sessions/session-cards.directive.html',
      scope: {
        sessions: '=',
        search: '='
      }
    };
  });
})();
