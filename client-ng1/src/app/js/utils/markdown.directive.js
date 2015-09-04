(function () {

  'use strict';

  angular.module('cesar-utils').directive('cesarMarkdown', function () {

    return {
      template: '<span ng-bind-html="format(content)"></span>',
      scope: {
        content : '='
      },
      replace : true,
      controller: function ($scope) {
        $scope.format = function(){
          return window.markdown.toHTML($scope.content);
        };
      }
    };

  });

})();
