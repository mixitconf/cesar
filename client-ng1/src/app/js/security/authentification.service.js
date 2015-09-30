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
        Session.create(response.data.login, response.data.name, response.data.email, response.data.roles);
        var date = new Date();
        var expireAt = new Date(date.getFullYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes()+25);
        $rootScope.$broadcast('event:auth-loginConfirmed', Session);
      }
    }

    function removeLocaleSession(response){
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
        .catch(removeLocaleSession);
    }

    function valid(authorizedRoles) {
      //We call the server to know if the user is authenticated
      $http
          .get('app/authenticated', {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function (response) {
            console.log('authenticated session %o %o', Session.login, response);
            if (!Session.login) {
              console.log('authenticated verify %o', Session.login);
              createSession(response);
            }
          })
          .catch(function(response){
            //If action is authorized for all no problem
            if(!authorizedRoles || authorizedRoles.indexOf(USER_ROLES.all)>=0){
              return;
            }
            //Else we have a problem
            removeLocaleSession(response);
          });

      console.log('valid %o', authorizedRoles);
      if(!authorizedRoles || authorizedRoles.indexOf(USER_ROLES.all)>=0){
        return;
      }

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