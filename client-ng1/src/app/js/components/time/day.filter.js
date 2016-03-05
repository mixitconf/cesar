(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-utils').filter('day', function ($filter) {
    'ngInject';

    return function(input){
      if(!input){
        return '';
      }
      return $filter('translate')('datetime.day' + moment(input).day()) + ' ' + moment(input).date();
    };
  });

})();
