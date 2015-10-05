(function () {

  'use strict';

  angular.module('cesar-security').controller('SecurityCtrl', function (AuthenticationService) {

    var ctrl = this;

    ctrl.login = AuthenticationService.login;

    ctrl.loginWithGoogle = function(){
      AuthenticationService.loginWithProvider('GOOGLE');
    }

    ctrl.loginWithTwitter = function(){
      AuthenticationService.loginWithProvider('TWITTER');
    }

  });

})();