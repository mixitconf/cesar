(function () {

  'use strict';

  angular.module('cesar-cfp').controller('AdminAccountsCtrl', function ($rootScope, $filter, $scope, accounts, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    ctrl.filter = function(){
      var accountsFiltered =  $filter('filter')(accounts, $scope.search);
      var accountsFiltered =  $filter('orderBy')(accountsFiltered, 'status');
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
