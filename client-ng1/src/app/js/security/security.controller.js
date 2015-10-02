(function () {

  'use strict';

  angular.module('cesar-security').controller('SecurityCtrl', function (AuthenticationService) {

    var ctrl = this;

    ctrl.login = function(){
      AuthenticationService.login(ctrl.credentials);
    };

  });

})();