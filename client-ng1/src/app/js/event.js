(function () {

  'use strict';
  /*global componentHandler */

  /**
   * Event handlers for errors (internal, security...)
   */
  angular.module('cesar').run(function ($rootScope, $state, $location, $timeout, $document, AuthenticationService, $translate) {
    'ngInject';

    var waitinPopupTimeout;
    $rootScope.waitingPopup = false;
    $rootScope.lang = $translate.preferredLanguage().slice(0,2);

    $rootScope.wait = function() {
      if(!waitinPopupTimeout){
        waitinPopupTimeout = $timeout(function(){
          //document.location.href='#top';
          $rootScope.waitingPopup = true;
        }, 100);
      }
    };

    $rootScope.stopWaiting = function() {
      $rootScope.waitingPopup = false;
      if(waitinPopupTimeout){
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
       angular.element(document.querySelector('.mdl-layout__drawer')).removeClass('is-visible');
      angular.element(document.querySelector('.mdl-layout__obfuscator')).removeClass('is-visible');

      if (next.name === 'logout') {
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
      $rootScope.userRefreshed = !$rootScope.userRefreshed;
    });

    $rootScope.$on('event:auth-changed', function () {
      $rootScope.userRefreshed = !$rootScope.userRefreshed;
    });

    $rootScope.$on('event:auth-logoutConfirmed', function () {
      $rootScope.userRefreshed = !$rootScope.userRefreshed;
    });

    //// Call when the 401 response is returned by the server
    $rootScope.$on('event:auth-loginRequired', function (event, next) {
      event.preventDefault();
      if($state.$current.url.sourcePath!=='/authent'){
      }
      else if(next.data){
        $rootScope.errorMessage = next.data.type;
      }
      return $state.go('authent');
    });


    //Refresh material design lite
    $rootScope.$on('$viewContentLoaded', function () {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      });
    });

    $rootScope.$on('event:language-changed', function (event, next) {
      $rootScope.lang = next.slice(0,2);
    });

  });

})();
