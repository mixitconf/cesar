(function () {

  'use strict';
  /*global componentHandler */

  /**
   * Event handlers for errors (internal, security...)
   */
  angular.module('cesar').run(function ($rootScope, $state, $location, $timeout, $document, AuthenticationService) {
    'ngInject';

    var waitinPopupTimeout;
    $rootScope.waitingPopup = false;

    $rootScope.wait = function() {
      if(!waitinPopupTimeout){
        waitinPopupTimeout = $timeout(function(){
          //document.location.href='#top';
          $rootScope.waitingPopup = true;
          angular.element(document.querySelector('body')).addClass('no-scrollable');
        }, 100);
      }
    };

    $rootScope.stopWaiting = function() {
      $rootScope.waitingPopup = false;
      if(waitinPopupTimeout){
        angular.element(document.querySelector('body')).removeClass('no-scrollable');
        $timeout.cancel(waitinPopupTimeout);
      }
    };

    //Error are catched to redirect user on error page
    $rootScope.$on('$cesarError', function (event, response) {
      $state.go('cerror', {error: response});
    });

    //When a ui-router state change we watch if user is authorized
    $rootScope.$on('$stateChangeStart', function (event, next) {

      //Patch to hide the drawer panel when a user click on a link (bug Material Design Lite)
      var node = document.querySelector('cesar-menu');
      angular.element(node.querySelector('.mdl-layout__drawer')).removeClass('is-visible');
      angular.element(node.querySelector('.mdl-layout__obfuscator')).removeClass('is-visible');

      if (next.name === 'logout' || next.name === 'createaccount') {
        AuthenticationService.logout();
      }
      else {
        AuthenticationService.valid(next.authorizedRoles);
      }
    });

    // Call when the the client is confirmed
    $rootScope.$on('event:auth-loginConfirmed', function () {
      if ($location.path() === '/authent') {
        var search = $location.search();
        if (search.redirect !== undefined) {
          $location.path(search.redirect).search('redirect', null).replace();
        }
        else {
          $location.path('/').replace();
        }
      }
    });

    //// Call when the 401 response is returned by the server
    $rootScope.$on('event:auth-loginRequired', function (event, next) {
      if ($location.path() !== '/authent') {
        var redirect = $location.path();
        $location.path('/authent').search('redirect', redirect).replace();
      }
      else {
        $rootScope.errorMessage = next.data.type;
      }
    });


    //Refresh material design lite
    $rootScope.$on('$viewContentLoaded', function () {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      });
    });


  });

})();
