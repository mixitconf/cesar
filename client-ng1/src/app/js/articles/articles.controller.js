(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticlesCtrl', function ($filter, $scope, articles, ArticleService) {
    'ngInject';

    var ctrl = this;
    var nbArticlePerPage = 3;
    var origin = angular.copy(articles);

    var id = origin[0].id;

    ctrl.ids = ArticleService.getClosestIds(origin, id, nbArticlePerPage);


    ctrl.displayMoreButton = function () {
      return origin[origin.length - 1].id !== ctrl.ids[nbArticlePerPage-1];
    };

    ctrl.moreArticle = function (articleId) {
      var newids = ArticleService.getPreviousIds(origin, articleId, nbArticlePerPage);
      if(newids){
        newids.forEach(function(elt){
          ctrl.ids.push(elt);
        });
      }

    };

    ctrl.filter = function(){
      var articlesFiltered =  $filter('filter')(articles, $scope.search);
      articlesFiltered =  $filter('orderBy')(articlesFiltered, '-postedAt');
      ctrl.pagination.nbtotal = articlesFiltered ? articlesFiltered.length : 0;
      ctrl.pagination.pages = Math.ceil(ctrl.pagination.nbtotal/ctrl.pagination.nbitems);
      return articlesFiltered;
    };

    ctrl.pagination = {
      current: 1,
      nbitems: 10
    };

    ctrl.displayItem = function (index){
      var min = ctrl.pagination.current * ctrl.pagination.nbitems - ctrl.pagination.nbitems;
      var max = ctrl.pagination.current * ctrl.pagination.nbitems - 1;
      return index>=min && index<=max;
    };

  });
})();
