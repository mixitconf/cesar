(function () {

  'use strict';

  angular.module('cesar-security').controller('PasswordLostCtrl', function (AuthenticationService, $state, $http) {
    'ngInject';

    var ctrl = this;

    ctrl.reinitPassword = function () {
      if (ctrl.credentials && ctrl.credentials.email) {

        $http.delete('/app/account/password?email=' + ctrl.credentials.email)
          .then(function () {
            $state.go('passwordlostconfirm');
          })
          .catch(function (response) {
            if (response.data.type==='EMAIL_EXIST'){
              ctrl.errorMessage = 'EMAIL_NOT_EXIST';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      }
    };
  });

})();