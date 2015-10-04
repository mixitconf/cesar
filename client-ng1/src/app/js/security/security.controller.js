(function () {

  'use strict';

  angular.module('cesar-security').controller('SecurityCtrl', function (AuthenticationService) {

    var ctrl = this;

    ctrl.login = AuthenticationService.login;
    ctrl.loginWithGoogle = AuthenticationService.loginWithGoogle;
    ctrl.loginWithTwitter = AuthenticationService.loginWithTwitter;

  });

})();