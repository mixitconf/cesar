(function () {

  'use strict';

  angular.module('cesar-account').controller('StatisticsCtrl', function ($http) {
    'ngInject';

    var ctrl = this;
    ctrl.stats = {};

    $http.get('/app/cfp/vote/nbsessions')
      .then(function (response) {
        ctrl.stats.nbtalks = response.data;
      });

    $http.get('/app/cfp/vote')
      .then(function (response) {
        ctrl.stats.raf = response.data;
      });


  });

})();