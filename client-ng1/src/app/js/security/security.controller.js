(function () {

  'use strict';

  angular.module('cesar-security').controller('SecurityCtrl', function (authService) {

    var ctrl = this;

    ctrl.login = function(){
      authService.login(ctrl.credentials);
    };

  });

})();