(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticlesCtrl', function (articles, ArticleService, paginationService) {
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

    ctrl.pagination = paginationService.createPagination('-postedAt');
    ctrl.pagination.set(articles);

  });
})();
