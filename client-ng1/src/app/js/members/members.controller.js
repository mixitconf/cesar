(function () {

  'use strict';

  angular.module('cesar-members').controller('MemberCtrl', function ($scope, $http, $state, cesarErrorService) {
    var ctrl = this;

    $http.get('/api/member/' + $state.current.data.member)
      .then(function (response) {
        ctrl.members = response.data;
      })
      .catch(cesarErrorService.throwError);

  });
})();