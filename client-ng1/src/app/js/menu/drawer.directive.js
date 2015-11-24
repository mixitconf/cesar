(function () {

  'use strict';

  angular.module('cesar-menu').directive('cesarDrawer', function () {
    return {
      templateUrl: 'js/menu/drawer.directive.html',
      controller: 'cesarMenuCtrl',
      controllerAs: 'ctrl'
    };
  });
})();
