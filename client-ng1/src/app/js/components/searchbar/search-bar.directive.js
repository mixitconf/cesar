(function () {

  'use strict';

  angular.module('cesar-menu').directive('cesarSearchBar', function () {
    'ngInject';

    return {
      templateUrl: 'js/components/searchbar/search-bar.directive.html',
      transclude:true,
      scope : {
        search:'='
      }
    };
  });
})();
