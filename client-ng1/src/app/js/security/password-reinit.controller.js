(function () {

  'use strict';

  angular.module('cesar-security').controller('PasswordReinitCtrl', function ($http, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.changePassword = function () {
      if (ctrl.credentials) {
        var data = 'actualpassword=' + encodeURIComponent(ctrl.credentials.actualpassword) + '&newpassword=' + encodeURIComponent(ctrl.credentials.password);
        $http
          .post('/app/account/password/reinit', data, {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            ignoreErrorRedirection: 'ignoreErrorRedirection'
          })
          .then(function () {
            $state.go('home');
          })
          .catch(function (response) {
            console.log(response.data.type)
            if (response.data.type==='BAD_CREDENTIALS'){
              ctrl.errorMessage = 'BAD_CREDENTIALS';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      }
    };
  });

})();