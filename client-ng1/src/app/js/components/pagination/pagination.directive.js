(function () {

  'use strict';

  angular.module('cesar-utils').directive('cesarPagination', function () {
    'ngInject';

    return {
      templateUrl: 'js/components/pagination/pagination.directive.html',
      scope: {
        pagination : '='
      }
    };
  });
})();
