(function () {

  'use strict';

  angular.module('cesar-articles').factory('ArticleService', function ($http) {

    function getAll(){
      return $http.get('/api/article');
    }

    function getById(id){
      return $http.get('/api/article/' + id);
    }

    return {
      getAll : getAll,
      getById : getById
    };
  });
})();