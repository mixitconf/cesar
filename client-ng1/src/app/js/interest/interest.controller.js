(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-planning').controller('InterestCtrl', function ($stateParams) {
    'ngInject';
    var ctrl = this;

    ctrl.name = $stateParams.name;
  });
})();
