(function () {

  'use strict';

  angular.module('cesar-sessions').factory( 'sessionscroller', function($timeout){
    return {
      get : function(index, count, success){
        $timeout(function(){
          var result = []
          result.push("item ##{i}")
          success(result);
        }, 100)
      }
    }
  }

  angular.module('cesar-sessions').directive('cesarSessionCards', function () {
    'ngInject';

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
