(function () {

  'use strict';

  angular.module('cesar-cfp').controller('AdminAccountsCtrl', function ($rootScope, $filter, $scope, $timeout, $http, accounts, account) {
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

    ctrl.filter = function(){
      var accountsFiltered =  $filter('filter')(accounts, $scope.search);
      accountsFiltered =  $filter('orderBy')(accountsFiltered, 'lastname');
      ctrl.pagination.nbtotal = accountsFiltered ? accountsFiltered.length : 0;
      ctrl.pagination.pages = Math.ceil(ctrl.pagination.nbtotal/ctrl.pagination.nbitems);
      return accountsFiltered;
    };

    ctrl.pagination = {
      current: 1,
      nbitems: 10
    };

    ctrl.displayItem = function (index){
      var min = ctrl.pagination.current * ctrl.pagination.nbitems - ctrl.pagination.nbitems;
      var max = ctrl.pagination.current * ctrl.pagination.nbitems - 1;
      return index>=min && index<=max;
    };

  });
})();
