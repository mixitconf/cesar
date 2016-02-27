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

    /**
     * We want to return {#nb} ids from article list. The first one is
     * {#articleId} and the 2 followers
     */
    function getClosestIds(articles, articleId, nb){
      var closestIds = [];
      //Article array is sorted from newer to older articles
      articles.forEach(function(elt){
        if(elt.id === articleId*1){
          closestIds.push(elt.id);
        }
        else if(closestIds.length>0 && closestIds.length<nb){
          closestIds.push(elt.id);
        }
      });

      return closestIds;
    }

    /**
     * Get next ids to see older articles
     */
    function getPreviousIds(articles, articleId, nb){
      var found = false;
      //Article array is sorted from newer to older articles
      var elts = articles.filter(function(elt){
        if(found){
          found = false;
          return true;
        }
        if(elt.id === articleId*1){
          found=true;
        }
        return false;
      });
      return (elts && elts.length>0) ? getClosestIds(articles, elts[0].id, nb) : [];
    }

    /**
     * Get next ids to see older articles
     */
    function getNextIds(articles, articleId, nb){
      var tabs = angular.copy(articles);
      tabs.reverse();
      var results = getPreviousIds(tabs, articleId, nb);
      return results ? results.reverse() : [];
    }

    return {
      getAll : getAll,
      getById : getById,
      getClosestIds: getClosestIds,
      getNextIds: getNextIds,
      getPreviousIds: getPreviousIds
    };
  });
})();