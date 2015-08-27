(function () {

  'use strict';

  angular.module('cesar-members').directive('cesarMemberSessionCards', function (SessionService, $log) {
    return {
      template: '<div><cesar-session-cards sessions="sessions"></cesar-session-cards></div>',
      scope: {
        idSessions: '=sessions'
      },
      controller: function ($scope) {
        $scope.sessions = [];
        if($scope.idSessions){
          $scope.idSessions.forEach(function(id){
            SessionService.getById(id).then(function(response){
              $scope.sessions.push(response.data);
            });
          });
        }
        else{
          $log.warn('This speaker has no session');
        }
      }
    };
  });
})();
