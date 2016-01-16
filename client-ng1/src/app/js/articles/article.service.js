(function () {

  'use strict';

  angular.module('cesar-articles').factory('ArticleService', function ($http) {
    'ngInject';


    function getAll(admin){
      return $http.get(admin ? '/app/article' : '/api/article');
    }

    function getById(id, admin){
      return $http.get(admin ? '/app/article/' + id : '/api/article/' + id);
    }


    return {
      getAll : getAll,
      getById : getById
    };
  });
})();