(function () {

  'use strict';

  angular.module('cesar-cfp').controller('AdminAccountsCtrl', function ($rootScope, accounts, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    ctrl.accounts = accounts;
  });
})();
