(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').controller('AdminCfpCtrl', function ($rootScope, $http, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }


    ctrl.refresh = function(){
      $http.get('app/cfp/proposal').then(
        function(response){
          ctrl.proposals = response.data;
        }
      );
    };

    ctrl.refresh();
  });
})();
