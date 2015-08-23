angular.module('cesar-members').directive('cesarMemberCards', function($window){
  "use strict";

  return {
    templateUrl: 'js/members/member-cards.directive.html',
    scope : {
      length : '@',
      members : '=',
      search : '=',
      limitText : '@'
    },
    controller : function($scope){
      if(!$scope.limitText){
        $scope.limitText = 300;
      }
      if(!$scope.length){
        $scope.length = 4;
      }
    }
  };
});
