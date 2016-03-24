(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-members').controller('MemberCtrl', function (member, $stateParams) {
    'ngInject';

    var ctrl = this;

    ctrl.member = member;
    ctrl.type = $stateParams.type;

  });

})();