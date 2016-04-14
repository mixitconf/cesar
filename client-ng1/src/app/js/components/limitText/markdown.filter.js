(function () {

  'use strict';

  angular.module('cesar-utils').filter('limitText', function () {
    'ngInject';

    return function(input, length){
      if(!input){
        return undefined;
      }
      return input.length <= length ? input : input.substring(0, length) + '...' ;
    };
  });

})();
