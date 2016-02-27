(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticleCtrl', function (articles, $stateParams, $state, $location, ArticleService) {
    'ngInject';

    var ctrl = this;
    var id = $stateParams.id;

    if(!$stateParams.id){
      $state.go('news', {'id': articles[0].id});
      return;
    }

    ctrl.id = id;

    ctrl.disableNextButton = function () {
      return articles[0].id+'' === ctrl.id;
    };

    ctrl.disablePreviousButton = function () {
      return articles[articles.length - 1].id+'' === ctrl.id;
    };

    ctrl.previousArticle = function () {
      ctrl.id = ArticleService.getPreviousIds(articles, ctrl.id, 1)[0];
    };

    ctrl.nextArticle = function () {
      ctrl.id = ArticleService.getNextIds(articles, ctrl.id, 1)[0];
    };

    ctrl.url = encodeURIComponent($location.absUrl());
  });
})();
