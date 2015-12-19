(function () {

  'use strict';

  angular.module('cesar-account').controller('CreateUserAccountCtrl', function ($http, $state, $translate, LANGUAGES) {
    'ngInject';

    var ctrl = this;
    ctrl.credentials = {
      defaultLanguage : ($translate.proposedLanguage()===LANGUAGES.en ? 'en' : 'fr')
    };

    ctrl.createUserAccount = function () {
      if (ctrl.credentials) {
        var credentialsTosend = angular.copy(ctrl.credentials);
        delete credentialsTosend.confirmpassword;

        $http
          .post('app/account/cesar', credentialsTosend, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('doneaction', {title : 'view.account.creation.title', description : 'view.account.creation.confirmation'});
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