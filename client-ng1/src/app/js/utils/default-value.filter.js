(function () {

  'use strict';

  angular.module('cesar-utils').filter('defaultValue', function () {
    'ngInject';

    return function(input, val){
      if(input){
        return input;
      }
      return val ? val : '???';
    };
  });
})();