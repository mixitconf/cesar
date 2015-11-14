(function () {

  'use strict';

  angular.module('cesar-account').controller('CreateUserAccountCtrl', function ($http, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.createUserAccount = function () {
      if (ctrl.credentials) {
        var credentialsTosend = angular.copy(ctrl.credentials);
        delete credentialsTosend.confirmpassword;

        $http
          .post('app/account/cesar', credentialsTosend, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('useraccountcreated');
          })
          .catch(function (response) {
            if (response.data.type) {
              switch (response.data.type) {
                case 'USER_EXIST':
                  ctrl.errorMessage = 'LOGIN_ALREADY_USED';
                  break;
                case   'EMAIL_EXIST':
                  ctrl.errorMessage = 'EMAIL_ALREADY_USED';
                  break;
              }
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      }
    };
  });

})();