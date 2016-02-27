(function () {

  'use strict';

  /**
   * Display an article
   */
  angular.module('cesar-articles').directive('cesarArticle', function () {
    'ngInject';

    return {
      templateUrl: 'js/article/article.directive.html',
      scope: {
        id: '='
      },
      controller: function($scope){


      }
    };
  });
})();
