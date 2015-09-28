(function () {

  'use strict';

  angular.module('cesar-security').factory('Session', function() {

    this.login = undefined;
    this.firstName = undefined;
    this.lastName = undefined;
    this.email = undefined;
    this.userRoles = undefined;

    this.invalidate = function () {
      this.login = null;
      this.firstName = null;
      this.lastName = null;
      this.email = null;
      this.userRoles =  null;
    };

    this.create = function (login, firstName, lastName, email, userRoles) {
      this.login = login;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.userRoles = userRoles;
    };

    return this;
  });

})();