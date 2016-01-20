(function () {

  'use strict';

  /**
   * This service is used to display a spinner when a request is executed
   */
  angular.module('cesar-utils').factory('cesarSpinnerService', function ($rootScope, $timeout) {
    'ngInject';

    var waitinPopupTimeout;
    $rootScope.waitingPopup = false;

    function wait() {
      if(!waitinPopupTimeout){
        waitinPopupTimeout = $timeout(function(){
          //document.location.href='#top';
          $rootScope.waitingPopup = true;
        }, 100);
      }
    };

    function stopWaiting() {
      $rootScope.waitingPopup = false;
      if(waitinPopupTimeout){
        $timeout.cancel(waitinPopupTimeout);
      }
    };

    return {
      activate: function(){
        $rootScope.spinner = 'on';
      },

      desactivate: function() {
        $rootScope.spinner = 'off';
      },

      wait:wait ,
      stopWaiting:stopWaiting
    };
  });

})();
