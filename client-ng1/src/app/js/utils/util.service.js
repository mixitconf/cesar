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
      },

      compareStr : function(a, b) {
        if(a && !b){
          return 1;
        }
        if(!a && b){
          return -1;
        }
        if(!a && !b || a.toLowerCase()===b.toLowerCase()){
          return 0;
        }
        if(a.toLowerCase()>b.toLowerCase()){
          return 1;
        }
        return -1;
      }
    };
  });

})();

