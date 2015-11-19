(function () {

  'use strict';

  angular.module('cesar-security').controller('PasswordReinitCtrl', function ($http, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.changePasword = function () {
      if (ctrl.credentials && ctrl.credentials.email) {
        $http
          .post('/app/account/password',
          {
            actualpassword : ctrl.credentials.actualpassword,
            newpassword : ctrl.credentials.password
          },
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            ignoreErrorRedirection: 'ignoreErrorRedirection'
          })
          .then(function () {
            $state.go('home');
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