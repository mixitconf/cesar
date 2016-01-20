(function () {

  'use strict';

  angular.module('cesar-home').controller('HomeCtrl', function (MemberService) {
    'ngInject';

    var ctrl = this;

    MemberService.getAll('sponsor', 2016)
      .then(function (response) {
        ctrl.sponsors = response.data;
      });
  });
})();