(function () {

  'use strict';

  angular.module('cesar-members').controller('MembersCtrl', function (MemberService, $state) {
    var ctrl = this;

    MemberService.getAll($state.current.data.member).then(function (response) {
      ctrl.members = response.data;
    });

  });
})();