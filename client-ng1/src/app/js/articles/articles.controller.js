(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('ArticlesCtrl', function (articles, $scope, $filter) {
    'ngInject';

    var ctrl = this;

    ctrl.articles = articles;

    ctrl.filter = function(){
      var articlesFiltered =  $filter('filter')(articles, $scope.search);
      var articlesFiltered =  $filter('orderBy')(articlesFiltered, '-postedAt');
      ctrl.pagination.nbtotal = articlesFiltered ? articlesFiltered.length : 0;
      ctrl.pagination.pages = parseInt(ctrl.pagination.nbtotal/ctrl.pagination.nbitems);
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
