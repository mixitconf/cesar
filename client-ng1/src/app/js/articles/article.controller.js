(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticleCtrl', function (articles, $stateParams, ArticleService) {
    var ctrl = this;
    var id = $stateParams.id ? $stateParams.id : articles[0].id;

    ctrl.articles = articles;
    ArticleService.getById(articles[0].id).then(function (response) {
      ctrl.article = response.data;
    });

    ctrl.displayNextButton = function(){
      return articles[0].id !== id;
    };
    ctrl.displayPreviousButton = function(){
      return articles[articles.length-1].id !== id;
    };

    ctrl.previousArticle = function(){
      console.log('TODO');
    };

    ctrl.nextArticle = function(){
      console.log('TODO');
    };
  });
})();
