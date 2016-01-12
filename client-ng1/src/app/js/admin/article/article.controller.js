(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('AdminArticleCtrl', function ($stateParams, ArticleService) {
    'ngInject';

    var ctrl = this;
    var id = $stateParams.id;

    ArticleService.getById(id).then(function (response) {
      ctrl.article = response.data;
    });


  });
})();
