(function () {

  'use strict';

  angular.module('cesar-security').factory('AuthenticationService', function ($rootScope, $http, $window, USER_ROLES, LocalStorageService) {
    'ngInject';

    function loginConfirmed(response){
      LocalStorageService.put('current-user', response.data);
      $rootScope.$broadcast('event:auth-loginConfirmed', LocalStorageService.get('current-user'));
    }

    function loginRequired(response){
      LocalStorageService.remove('current-user');
      $rootScope.$broadcast('event:auth-loginRequired', response);
    }

    function valid(authorizedRoles, preLoadSecurity) {
      if(preLoadSecurity){
        $http.get('app/login-finalize').then(loginConfirmed);
      }

      //We don't need to call the server at everytime. We see if user is stored in local storage
      var currentUser = LocalStorageService.get('current-user');

      //If screen has a restrictive access we need to control the rights
      if(authorizedRoles && authorizedRoles.indexOf(USER_ROLES.all)<0) {
        //If user is not present the user has to login
        if(!currentUser || !currentUser.roles){
          loginRequired();
          return;
        }
        //If user has'nt the right an exception is thrown
        if(!isAuthorized(authorizedRoles, currentUser)){
          $rootScope.$broadcast('event:auth-notAuthorized');
        }
      }
      //If user has no right for the screen an event is launched just before. We also need to pass
      //the current user to the app
      if(currentUser!==null && currentUser){
        loginConfirmed({ data : currentUser});
      }
    }

    function isAuthorized(authorizedRoles, currentUser) {
      if (!angular.isArray(authorizedRoles)) {
        authorizedRoles = [authorizedRoles];
      }

      var isAuth = false;
      angular.forEach(authorizedRoles, function (authorizedRole) {
        var authorized = (!!currentUser.oauthId && currentUser.roles.indexOf(authorizedRole) !== -1);
        if (authorized || authorizedRole === '*') {
          isAuth = true;
        }
      });

      return isAuth;
    }

    function logout() {
      LocalStorageService.remove('current-user');
      $rootScope.$broadcast('event:auth-loginCancelled');
    }

    function login(param) {
      var data = 'username=' + encodeURIComponent(param.username) + '&password=' + encodeURIComponent(param.password);
      $http
        .post('app/login', data, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          ignoreErrorRedirection: 'ignoreErrorRedirection'
        })
        .then(loginConfirmed)
        .catch(loginRequired);
    }


    function loginWithProvider(provider) {
      $window.location.href = '/app/login-with/' + provider;
    }

    function createUserAccount(credentials){
      delete credentials.confirmpassword;
      return $http
        .post('app/account/create', credentials, {ignoreErrorRedirection: 'ignoreErrorRedirection'});
    }

    return {
      'login': login,
      'loginWithProvider' : loginWithProvider,
      'valid': valid,
      'logout': logout,
      'createUserAccount': createUserAccount
    };
  });

})();