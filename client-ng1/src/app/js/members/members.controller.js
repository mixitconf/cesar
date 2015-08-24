(function () {

  'use strict';

  angular.module('cesar-members').controller('MemberCtrl', function ($http, $state) {
    var ctrl = this;

    $http.get('/api/member/' + $state.current.data.member).then(function (response) {
      ctrl.members = response.data;
    });

  });
})();