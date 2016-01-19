(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('CacheCtrl', function ($http) {
    'ngInject';

    var ctrl = this;

    ctrl.refresh = function () {
      $http.get('/monitoring/metrics')
        .then(function (response) {
          ctrl.result = {};

          Object.keys(response.data).forEach(function (elt) {
            if (elt.indexOf('cache') >= 0) {
              ctrl.result[elt] = response.data[elt];
            }
          });
        });
    };

    ctrl.clear = function (area) {
      $http.delete('/app/cache/' + area)
        .then(function () {
          ctrl.purgeResult = 'OK';
        })
        .catch(function () {
          ctrl.purgeResult = 'KO';
        });
      ctrl.refresh();
    };

    ctrl.refresh();
  });
})();
