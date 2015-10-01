(function () {

  'use strict';

  angular.module('cesar-utils').factory('LocalStorageService', function ($window) {

    var put = function(key, value){
      $window.localStorage.setItem(key, angular.toJson(value));
    };

    var read = function(key){
      var json = $window.localStorage.getItem(key);
      return angular.fromJson(json);
    };

    var remove = function(key){
      $window.localStorage.removeItem(key);
    };

    return {
      put: put,
      get: read,
      remove : remove
    };
  });

})();