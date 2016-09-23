(function () {

  'use strict';

  angular.module('cesar-home').controller('HomeCtrl', function (MemberService) {
    'ngInject';

    var ctrl = this;

    MemberService.getAll('sponsor', 2017)
      .then(function (response) {
        ctrl.homesponsors = response.data;
      });
  });
})();