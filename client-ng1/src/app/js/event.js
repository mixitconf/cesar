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
      $state.go('error', {error: response});
    });

    //When a ui-router state change we watch if user is authorized
    $rootScope.$on('$stateChangeStart', function (event, next) {
      if (next.name === 'logout' || next.name === 'createuseraccount') {
        AuthenticationService.logout();
      }
      else {
        AuthenticationService.valid(next.authorizedRoles, next.name === 'createaccount' || next.name === 'account');
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
        switch (next.data.type) {
          case 'BAD_CREDENTIALS':
            $rootScope.errorMessage = 'Le mot de passe est incorrect';
            break;
          case   'REQUIRED_ARGS':
            $rootScope.errorMessage = 'Le login et le mot de passe son obligatoires';
            break;
          case   'USER_NOT_FOUND':
            $rootScope.errorMessage = 'Ce login n\'existe pas. Veuillez créer un compte pour vous connecter';
            break;
          default:
            $rootScope.errorMessage = 'Erreur détectée. Veuillez réessayer ou contacter l\'équipe Mix-IT pour nous remonter ce bug';
        }
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
