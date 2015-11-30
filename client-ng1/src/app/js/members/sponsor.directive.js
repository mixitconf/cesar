(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarSponsor', function () {

    return {
      templateUrl: 'js/members/sponsor.directive.html',
      scope : {
        members: '=',
        level: '@'
      }
    };
  });
})();
