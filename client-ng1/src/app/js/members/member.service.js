(function () {

  'use strict';

  angular.module('cesar-members').factory('MemberService', function ($http) {

    function getAll(type){
      return $http.get('/api/member/' + type);
    }

    function getById(id){
      return $http.get('/api/member/' + id);
    }

    return {
      getAll : getAll,
      getById : getById
    };
  });
})();