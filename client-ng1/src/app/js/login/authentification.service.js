(function () {

  'use strict';

  angular.module('cesar-security').factory('AuthenticationService', function ($rootScope, $http, $window, $translate, USER_ROLES, LANGUAGES, LocalStorageService) {
    'ngInject';

    function currentUser(){
      return $http.get('app/account/check', {ignoreErrorRedirection: 'ignoreErrorRedirection'})
        .then(function(response){
          LocalStorageService.put('current-user', response.data);
          return response.data;
        })
        .catch(function(){
          return undefined;
        });
    }

    function loginConfirmed(response){
      if(response.data && response.data.defaultLanguage){
        $translate.use((response.data.defaultLanguage === 'en') ? LANGUAGES.en : LANGUAGES.fr);
      }
      LocalStorageService.put('current-user', response.data);
      $rootScope.$broadcast('event:auth-loginConfirmed');
    }

    function loginRequired(response){
      LocalStorageService.remove('current-user');
      $rootScope.$broadcast('event:auth-loginRequired', response);
    }

    function logout() {
      currentUser().then(function(currentUser) {
        if (currentUser) {
          $http.get('app/logout');
          LocalStorageService.remove('current-user');
          $rootScope.$broadcast('event:auth-logoutConfirmed');
        }
      });
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


    function loginWithProvider(provider, redirect) {
      $rootScope.spinner='on';
      $window.location.href = '/app/login-with/' + provider + (redirect ? '?to=' + redirect : '');
    }

    function checkUser(){
      $http.get('app/account/check')
        .then(loginConfirmed)
        .catch(loginRequired);
    }

    function valid(authorizedRoles) {
      //We don't need to call the server at everytime. We see if user is stored in local storage
      var currentUser = LocalStorageService.get('current-user');

      //If screen has a restrictive access we need to control the rights
      if(authorizedRoles && authorizedRoles.indexOf(USER_ROLES.all)<0) {
        //If user is not present the user has to login
        if(!currentUser || !currentUser.roles){
          checkUser();
          return;
        }
        //If user has'nt the right an exception is thrown
        if(!isAuthorized(authorizedRoles, currentUser)){
          $rootScope.$broadcast('event:auth-loginRequired');
        }
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

    return {
      'currentUser': currentUser,
      'checkUser': checkUser,
      'isAuthorized': isAuthorized,
      'login': login,
      'loginWithProvider' : loginWithProvider,
      'valid': valid,
      'logout': logout
    };
  });

})();