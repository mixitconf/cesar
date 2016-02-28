(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticleCtrl', function (articles, $stateParams, $state, $location, ArticleService) {
    'ngInject';

    var ctrl = this;
    var nbArticlePerPage = 3;

    if(!$stateParams.id){
      $state.go('news', {'id': articles[0].id});
      return;
    }

    ctrl.ids = ArticleService.getClosestIds(articles, $stateParams.id, nbArticlePerPage);

    ctrl.disableNextButton = function () {
      return articles[0].id === ctrl.ids[0];
    };

    ctrl.disablePreviousButton = function () {
      return articles[articles.length - 1].id === ctrl.ids[nbArticlePerPage-1];
    };

    ctrl.previousArticle = function (articleId) {
      ctrl.ids = ArticleService.getPreviousIds(articles, articleId, nbArticlePerPage);
    };

    ctrl.nextArticle = function (articleId) {
      ctrl.ids = ArticleService.getNextIds(articles, articleId, nbArticlePerPage);
    };

    ctrl.url = encodeURIComponent($location.absUrl());
  });
})();
