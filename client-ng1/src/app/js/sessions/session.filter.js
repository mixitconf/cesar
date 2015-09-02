(function () {

  'use strict';

  angular.module('cesar-sessions').filter('sessionLogo', function () {
    return function(input){
      switch(input){
        case 'Talk':
          return 'local_library';
        case 'Workshop':
          return 'build';
        case 'Keynote':
          return 'face';
        default:
          return 'forward_5';
      }
    };
  });
})();