(function () {

  'use strict';

  angular.module('cesar-security').controller('ReinitPasswordAccountCtrl', function (AuthenticationService, $scope, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.reinitPassword = function () {

      //TODO
      if (ctrl.credentials) {
        AuthenticationService
          .createUserAccount(angular.copy(ctrl.credentials))
          .then(function () {
            $state.go('useraccountcreated');
          })
          .catch(function (response) {
            console.log('Error %o', response);
            if (response.data.type) {
              switch (response.data.type) {
                case 'USER_EXIST':
                  ctrl.errorMessage = 'Ce login est déjà utilisé';
                  break;
                case   'EMAIL_EXIST':
                  ctrl.errorMessage = 'Cet email est déjà utilisé. Si c\'est le votre cliquez sur le lien pour réinitialiser votre mot de passe sur la page de login';
                  break;
              }
            }
            else {
              ctrl.errorMessage = 'Erreur détectée. Veuillez réessayer ou contacter l\'équipe Mix-IT pour nous remonter ce bug';
            }
          });
      }
    };
  });

})();