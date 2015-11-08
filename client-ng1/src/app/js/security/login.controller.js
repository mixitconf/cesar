(function () {

  'use strict';

  angular.module('cesar-security').controller('LoginCtrl', function (AuthenticationService, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.login = AuthenticationService.login;

    ctrl.loginWithGoogle = function(){
      AuthenticationService.loginWithProvider('GOOGLE', $state.redirect);
    };

    ctrl.loginWithTwitter = function(){
      AuthenticationService.loginWithProvider('TWITTER', $state.redirect);
    };

  });

})();