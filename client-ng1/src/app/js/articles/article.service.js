(function () {

  'use strict';

  angular.module('cesar-articles').factory('ArticleService', function ($http) {

    function getAll(){
      return $http.get('/crud/article');
    }

    function getById(id){
      return $http.get('/crud/article/' + id);
    }

    return {
      getAll : getAll,
      getById : getById
    };
  });
})();