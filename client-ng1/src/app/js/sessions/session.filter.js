(function () {

  'use strict';

  angular.module('cesar-sessions').filter('sessionLogo', function () {
    'ngInject';

    return function(input){
      switch(input){
        case 'Talk':
          return 'local_library';
        case 'Workshop':
          return 'build';
        case 'Keynote':
          return 'face';
        case 'Random':
          return 'help';
        default:
          return 'forward_5';
      }
    };
  });
})();