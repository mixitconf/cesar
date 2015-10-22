(function () {

  'use strict';

  angular.module('cesar-utils').factory('Util', function () {
    'ngInject';

    return {
      extractId: function(url){
        if(url){
          var elements = url.split('/');
          return elements[elements.length-1];
        }
        return undefined;
      }
    };
  });

})();

