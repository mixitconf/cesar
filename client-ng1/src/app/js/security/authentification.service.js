(function () {

  'use strict';

  angular.module('cesar-security').factory('authService', function ($rootScope, $http, $cookies, Session, USER_ROLES) {

    //TODO delete console.log

    function createSession(response){
      console.log('create session %o', response.data);
      if(!response.data.login){
        removeSession();
      }
      else {
        Session.create(response.data.login, response.data.firstName, response.data.lastName, response.data.email, response.data.roles);
        var date = new Date();
        var expireAt = new Date(date.getFullYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes()+25);
        $cookies.put('cesarTokenCookie', response.data.token);
        $rootScope.$broadcast('event:auth-loginConfirmed', Session);
      }
    }

    function removeSession(response){
      console.log(response);
      Session.invalidate();
      $rootScope.$broadcast('event:auth-loginRequired');
    }

    function login(param) {
      console.log('login %o', param);
      var data = 'username=' + encodeURIComponent(param.username) + '&password=' + encodeURIComponent(param.password);
      $http
        .post('app/login', data, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          ignoreErrorRedirection: 'ignoreErrorRedirection'
        })
        .then(createSession)
        .catch(removeSession);
    }

    function valid(authorizedRoles) {
      console.log('valid %o', authorizedRoles);
      if(!authorizedRoles || authorizedRoles.indexOf(USER_ROLES.all)>=0){
        return;
      }
      $http
        .get('app/authenticated', { ignoreErrorRedirection: 'ignoreErrorRedirection'})
        .then(function () {
          console.log('authenticated session %o', Session.login);
          if (!Session.login) {
            console.log('authenticated verify %o', Session.login);
            $http.get('app/login').then(createSession);
          }
        })
        .catch(removeSession);
    }

    function isAuthorized(authorizedRoles) {
      console.log('isAuth %o', authorizedRoles);
      if (!angular.isArray(authorizedRoles)) {
        authorizedRoles = [authorizedRoles];
      }

      var isAuth = false;
      angular.forEach(authorizedRoles, function (authorizedRole) {
        var authorized = (!!Session.login && Session.userRoles.indexOf(authorizedRole) !== -1);
        if (authorized || authorizedRole === '*') {
          isAuth = true;
        }
      });

      return isAuth;
    }


    function logout() {
      console.log('logout');
      $cookies.remove('cesarTokenCookie');
      $http.get('app/logout');
      Session.invalidate();
      $rootScope.$broadcast('event:auth-loginCancelled');
    }

    return {
      'login': login,
      'valid': valid,
      'isAuthorized': isAuthorized,
      'logout': logout
    };
  });

})();