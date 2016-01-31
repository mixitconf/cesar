(function () {

  'use strict';

  angular.module('cesar-cfp').controller('AdminPlanningCtrl', function ($rootScope, account) {
    'ngInject';


    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

  });
})();
