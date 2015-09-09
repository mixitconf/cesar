(function () {

  'use strict';

  angular.module('cesar-sessions').factory('SessionService', function ($http) {

    function getAllByYear(year){
      return $http.get('/api/session?year=' + year);
    }

    function getAll(type){
      return $http.get('/api/session/' + type);
    }

    function getById(id){
      return $http.get('/api/session/' + id);
    }

    return {
      getAll : getAll,
      getAllByYear : getAllByYear,
      getById : getById
    };
  });
})();