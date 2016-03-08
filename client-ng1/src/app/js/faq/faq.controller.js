(function () {

  'use strict';

  angular.module('cesar-security').controller('FaqCtrl', function (AuthenticationService, $location, $anchorScroll) {
    'ngInject';

    var ctrl = this;

    ctrl.goto = function(link){
      $location.hash(link);
      $anchorScroll();
    };

  });

})();