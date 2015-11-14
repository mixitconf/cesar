(function () {

  'use strict';

  angular.module('cesar-account').controller('AccountCtrl', function ($http, $state, account) {
    'ngInject';

    var ctrl = this;
    ctrl.account = account;

    ctrl.updateAccount = function () {
      $http
          .put('app/account', angular.copy(ctrl.account), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
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

  });

})();