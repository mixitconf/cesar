(function () {

  'use strict';

  angular.module('cesar-utils').filter('markdown', function ($sanitize) {
    'ngInject';

    return function(input){
      if(!input){
        return undefined;
      }
      return $sanitize(window.markdown.toHTML(input));
    };
  });

})();
