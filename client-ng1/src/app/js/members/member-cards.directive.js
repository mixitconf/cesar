(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarMemberCards', function () {
    'ngInject';

    return {
      templateUrl: 'js/members/member-cards.directive.html',
      scope: {
        length: '@',
        lengthPhone:'@',
        members: '=',
        search: '=',
        limitText: '@',
        type: '@'
      },
      controller: function ($scope) {
        $scope.order = $scope.type === 'sponsor' ? 'level[0].value' : 'firstname';
        if (!$scope.limitText) {
          $scope.limitText = 300;
        }
        if (!$scope.length) {
          $scope.length = 4;
        }
        if(!$scope.lengthPhone){
          $scope.lengthPhone=$scope.length;
        }
      }
    };
  });
})();
