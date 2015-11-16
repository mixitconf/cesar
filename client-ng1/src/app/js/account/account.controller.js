(function () {

  'use strict';

  angular.module('cesar-account').controller('AccountCtrl', function ($http, $state, $translate, account, LANGUAGES) {
    'ngInject';

    var ctrl = this;
    ctrl.account = account;

    if(ctrl.account){
      ctrl.updateAccount = function () {
        $http
          .put('app/account', angular.copy(ctrl.account), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            if(ctrl.account.defaultLanguage){
              $translate.use((ctrl.account.defaultLanguage === LANGUAGES.us) ? LANGUAGES.fr : LANGUAGES.us);
            }
            $state.go('home');
          })
          .catch(function (response) {
            if (response.data.type && response.data.type === 'USER_NOT_FOUND'){
              ctrl.errorMessage = 'USER_NOT_FOUND';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      };
    }


  });

})();