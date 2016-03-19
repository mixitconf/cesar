(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarMemberSessionCards', function (SessionService, $log) {
    'ngInject';

    return {
    templateUrl: 'js/members/member-session-cards.directive.html',
      scope: {
        idSessions: '=sessions',
        current: '=',
        firstname: '='
      },
      controller: function ($scope) {
        $scope.sessions = [];
        if ($scope.idSessions) {
          $scope.idSessions.forEach(function (id) {
            SessionService.getById(id).then(function (response) {
              if (!$scope.current || response.data.year === $scope.current){
                $scope.sessions.push(response.data);
              }
            });
          });
        }
        else {
          $log.warn('This speaker has no session');
        }
      }
    };
  });
})();
