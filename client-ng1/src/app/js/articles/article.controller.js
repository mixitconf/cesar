(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticleCtrl', function (articles, $stateParams, $state, ArticleService) {
    var ctrl = this;
    var id = $stateParams.id;

    if(!$stateParams.id){
      $state.go('news', {'id': articles[0].id});
      return;
    }

    ctrl.articles = articles;
    ctrl.id = id;
    //TODO if I am the creator of an article I need to be able to edit it or delete it
    //TODO A moderator can delete one article
    //TODO wait for rigths and profiles

    ArticleService.getById(id).then(function (response) {
      ctrl.article = response.data;
    });

    ctrl.disableNextButton = function () {
      return articles[0].id+'' === id;
    };
    ctrl.disablePreviousButton = function () {
      return articles[articles.length - 1].id+'' === id;
    };

    ctrl.previousArticle = function () {
      var nextToSend = false;
      articles.forEach(function (article) {
        if (nextToSend) {
          $state.go('news', {'id': article.id});
          nextToSend = false;
        }
        if (article.id + '' === id) {
          nextToSend = true;
        }
      });
    };

    ctrl.nextArticle = function () {
      var previous;
      articles.forEach(function (article) {
        if (article.id + '' === id) {
          $state.go('news', {'id': previous.id});
        }
        previous = article;
      });
    };

    ctrl.addComment = function(){
      console.log('TODO implement this feature');
    };
  });
})();
