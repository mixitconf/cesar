(function () {

  'use strict';

  angular.module('cesar-home').controller('HomeCtrl', function ($rootScope, MemberService) {
    'ngInject';

    var ctrl = this;

    $rootScope.wait();
    MemberService.getAll('sponsor', 2016)
      .then(function (response) {
        ctrl.sponsors = response.data;
      })
      .finally($rootScope.stopWaiting);

  });
})();