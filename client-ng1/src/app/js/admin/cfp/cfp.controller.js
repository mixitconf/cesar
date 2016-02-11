(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').controller('AdminCfpCtrl', function ($rootScope, $filter, $http, $scope, account) {
    'ngInject';

    var ctrl = this;
    var proposals;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }


    ctrl.refresh = function(){
      $http.get('app/cfp/proposal').then(
        function(response){
          proposals = response.data;
        }
      );
    };

    ctrl.filter = function(){
      var proposalsFiltered =  $filter('filter')(proposals, $scope.search);
      var proposalsFiltered =  $filter('orderBy')(proposalsFiltered, 'status');
      ctrl.pagination.nbtotal = proposalsFiltered ? proposalsFiltered.length : 0;
      ctrl.pagination.pages = Math.ceil(ctrl.pagination.nbtotal/ctrl.pagination.nbitems);
      return proposalsFiltered;
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

    ctrl.refresh();
  });
})();
