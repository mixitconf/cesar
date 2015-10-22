(function () {

  'use strict';

  angular.module('cesar-menu').directive('cesarSearchBar', function () {
    'ngInject';

    return {
      templateUrl: 'js/menu/search-bar.directive.html',
      transclude:true
    };
  });
})();
