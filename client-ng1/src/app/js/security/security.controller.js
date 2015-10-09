(function () {

  'use strict';

  angular.module('cesar-security').controller('SecurityCtrl', function (AuthenticationService) {

    var ctrl = this;

    ctrl.login = AuthenticationService.login;

    ctrl.loginWithGoogle = function(){
      AuthenticationService.loginWithProvider('GOOGLE');
    };

    ctrl.loginWithTwitter = function(){
      AuthenticationService.loginWithProvider('TWITTER');
    };

    ctrl.createNewAccount = function(){
      //We need to logout the user
      AuthenticationService.logout();
    };

    ctrl.createUserAccount = function(){
      AuthenticationService.createUserAccount(angular.copy(ctrl.credentials));
    };
  });

})();