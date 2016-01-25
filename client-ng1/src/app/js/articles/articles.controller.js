(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticlesCtrl', function (articles) {
    'ngInject';

    var ctrl = this;

    ctrl.articles = articles;
  });
})();
