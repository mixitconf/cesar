(function () {

  'use strict';
  /*global componentHandler */

  /**
   * Event handlers for errors (internal, security...)
   */
  angular.module('cesar').run(function ($rootScope, $state, $location, $timeout, AuthenticationService) {
    'ngInject';

    //Error are catched to redirect user on error page
    $rootScope.$on('$cesarError', function (event, response) {
      $state.go('cerror', {error: response});
    });

    //When a ui-router state change we watch if user is authorized
    $rootScope.$on('$stateChangeStart', function (event, next) {
      if (next.name === 'logout' || next.name === 'createaccount') {
        AuthenticationService.logout();
      }
      else {
        AuthenticationService.valid(next.authorizedRoles);
      }
    });

    // Call when the the client is confirmed
    $rootScope.$on('event:auth-loginConfirmed', function (event, next) {
      if (!$rootScope.userConnected || (next && next.oauthId !== $rootScope.userConnected.oauthId)) {
        $rootScope.userConnected = next;
      }

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



    // Call when the user logs out
    $rootScope.$on('event:auth-loginCancelled', function () {
      delete  $rootScope.userConnected;
    });


    //Refresh material design lite
    $rootScope.$on('$viewContentLoaded', function () {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      });
    });

  });

})();
