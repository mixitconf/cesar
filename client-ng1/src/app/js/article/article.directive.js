(function () {

  'use strict';

  angular.module('cesar-articles').directive('cesarArticle', function (ArticleService, $location, $rootScope) {
    'ngInject';

    return {
      templateUrl: 'js/article/article.directive.html',
      scope: {},
      bindToController: {
        id: '='
      },
      controllerAs: 'ctrl',
      controller: CesarArticleCtrl,
      link: CesarArticleLinker
    };

    function CesarArticleCtrl(){
      var ctrl = this;

      ctrl.refresh = function(id){
        ArticleService.getById(id).then(function (response) {
          ctrl.article = response.data;
        });
      };

      ctrl.refresh(ctrl.id);

      ctrl.url = encodeURIComponent($location.absUrl()) + '/' + ctrl.id;
    }

    function CesarArticleLinker(scope, el, attr, ctrl){
      scope.$watch('ctrl.id', function(newId){
        ctrl.refresh(newId);
      });

      $rootScope.$watch('lang', function(lang){
        scope.lang = lang;
      });
    }
  });
})();
