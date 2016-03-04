(function () {

  'use strict';

  angular.module('cesar-cfp').controller('AdminAccountsCtrl', function ($rootScope, $timeout, $http, accounts, account, paginationService) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    ctrl.accountPurge = function(){
      $http.get('app/account/purge')
        .then(function () {
          ctrl.purgeOk = 'La purge des comptes non rattachés est terminée';
          $http.get('/app/account').then(function (response) {
            accounts = response.data;
            $timeout(function(){
              delete ctrl.purgeOk;
            }, 3000);
          });
        });
    };

    ctrl.pagination = paginationService.createPagination('lastname');
    ctrl.pagination.set(accounts);

  });
})();
