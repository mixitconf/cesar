(function () {

  'use strict';

  angular.module('cesar-account').controller('CreateSocialAccountCtrl', function ($http, $state, $translate, LANGUAGES) {
    'ngInject';

    var ctrl = this;

    ctrl.credentials = {
      defaultLanguage : ($translate.proposedLanguage()===LANGUAGES.en ? 'en' : 'fr')
    };

    ctrl.createSocialAccount = function () {
      if (ctrl.credentials) {
        $http
          .put('app/account/social', angular.copy(ctrl.credentials), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('doneaction', {title : 'view.account.creation.title', description : 'view.account.creation.confirmation'});
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