(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-members').controller('MemberCtrl', function (AuthenticationService, USER_ROLES, member, $stateParams) {
    'ngInject';

    var ctrl = this;

    ctrl.member = member;
    ctrl.type = $stateParams.type;

    AuthenticationService.currentUser().then(function (currentUser) {
      if (!currentUser || !AuthenticationService.isAuthorized(USER_ROLES.admin, currentUser)) {
        delete ctrl.member.email;
      }
    })
    .catch(function(){
      delete ctrl.member.email;
    });
  });

})();