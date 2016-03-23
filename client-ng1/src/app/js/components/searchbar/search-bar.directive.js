(function () {

  'use strict';

  angular.module('cesar-menu').directive('cesarSearchBar', function ($stateParams, $state) {
    'ngInject';

    return {
      templateUrl: 'js/components/searchbar/search-bar.directive.html',
      transclude: true,
      scope: {
        search: '=',
        state: '@'
      },
      controller: function ($scope) {
        if ($stateParams.search) {
          $scope.search = $stateParams.search;
        }

        $scope.updateSearch = function () {
          $stateParams.search = $scope.search;
          $state.go($state.current.name, $stateParams, {reloadOnSearch: false, notify:false});
        };
      }
    };
  });
})();
