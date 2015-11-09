(function () {

  'use strict';

  angular.module('cesar-security').controller('CreateSocialAccountCtrl', function ($http, $scope, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.createSocialAccount = function () {
      if (ctrl.credentials) {
        $http
          .post('app/account/social', angular.copy(ctrl.credentials), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('useraccountcreated');
          })
          .catch(function (response) {
            if (response.data.type && response.data.type === 'EMAIL_EXIST'){
              ctrl.errorMessage = 'EMAIL_ALREADY_USED';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      }
    };
  });

})();