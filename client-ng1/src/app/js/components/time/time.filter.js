(function () {

  'use strict';

  angular.module('cesar-utils').filter('time', function () {
    'ngInject';

    return function(input){
      if(!input){
        return '';
      }
      return moment(input).format('HH:mm');
    };
  });

})();
