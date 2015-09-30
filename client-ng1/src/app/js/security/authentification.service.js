(function () {

  'use strict';

  angular.module('cesar-security').factory('AuthenticationService', function ($rootScope, $http, $cookies, USER_ROLES) {

    var currentUser;

    function loginConfirmed(response){
      if(!response.data.login){
        loginRequired();
      }
      else {
        $rootScope.$broadcast('event:auth-loginConfirmed', response.data);
      }
    }

    function loginRequired(){
      currentUser = null;
      $rootScope.$broadcast('event:auth-loginRequired');
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

    function valid(authorizedRoles) {
      //We call the server to know if the user is authenticated
      $http
          .get('app/authenticated', {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(loginConfirmed)
          .catch(function(response){
            //If action is authorized for all no problem
            if(authorizedRoles && authorizedRoles.indexOf(USER_ROLES.all)<0) {
              loginRequired(response);
            }
          });
    }

    function isAuthorized(authorizedRoles) {
      if (!angular.isArray(authorizedRoles)) {
        authorizedRoles = [authorizedRoles];
      }

      var isAuth = false;
      angular.forEach(authorizedRoles, function (authorizedRole) {
        var authorized = (!!currentUser.login && currentUser.roles.indexOf(authorizedRole) !== -1);
        if (authorized || authorizedRole === '*') {
          isAuth = true;
        }
      });

      return isAuth;
    }


    function logout() {
      console.log('logout');
      $http.get('app/logout');
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