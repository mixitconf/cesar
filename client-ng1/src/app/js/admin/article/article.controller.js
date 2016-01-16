(function () {

  'use strict';
  /*global componentHandler */

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('AdminArticleCtrl', function ($stateParams, $state, $http,$timeout, ArticleService) {
    'ngInject';

    var ctrl = this;
    var id = $stateParams.id;

    if(id){
      ArticleService.getById(id, true).then(function (response) {
        ctrl.article = response.data;
        refresh();
      });
    }
    else{
      ctrl.article = {};
    }


    ctrl.save = function(){
      if(ctrl.article){
        $http
            .post('/app/article', ctrl.article, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
            .then(function () {
              $state.go('admarticles');
            })
            .catch(function () {
              ctrl.errorMessage = 'UNDEFINED';
            });
      }
    };

    function refresh() {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      }, 1000);
    }
  });
})();
