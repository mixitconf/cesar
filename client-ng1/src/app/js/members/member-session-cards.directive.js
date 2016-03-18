(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarMemberSessionCards', function (SessionService, $log) {
    'ngInject';

    return {
      template: '<div><cesar-session-cards sessions="sessions" display="minimal"></cesar-session-cards></div>',
      scope: {
        idSessions: '=sessions',
        current: '='
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
