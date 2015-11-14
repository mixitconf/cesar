(function () {

  'use strict';

  angular.module('cesar-security').controller('DoneActionCtrl', function ($stateParams, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.title = $state.params.title;
    ctrl.description = $state.params.description;
  });

})();