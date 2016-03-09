(function () {

  'use strict';

  angular.module('cesar-account').controller('StatisticsCtrl', function ($http) {
    'ngInject';

    var ctrl = this;
    ctrl.stats = {};

    $http.get('/app/cfp/vote/nbsessions/SUBMITTED')
      .then(function (response) {
        ctrl.stats.submitted = response.data;
      });
    $http.get('/app/cfp/vote/nbsessions/ACCEPTED')
      .then(function (response) {
        ctrl.stats.accepted = response.data;
      });
    $http.get('/app/cfp/vote/nbsessions/REJECTED')
      .then(function (response) {
        ctrl.stats.rejected = response.data;
      });

    $http.get('/app/cfp/vote')
      .then(function (response) {
        ctrl.stats.dones = response.data;
      });


  });

})();