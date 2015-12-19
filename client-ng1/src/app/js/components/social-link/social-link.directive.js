(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarSocialLink', function () {

    return {
      templateUrl: 'js/components/social-link/social-link.directive.html',
      scope: {
        classNames: '@',
        url: '='
      },
      controller: function ($scope) {
        'ngInject';

        if ($scope.url && $scope.url.indexOf('twitter') > 0) {
          $scope.imgtype = 'twitter';
        }
        else if ($scope.url && $scope.url.indexOf('google') > 0) {
          $scope.imgtype = 'google';
        }
      }
    };
  });
})();
