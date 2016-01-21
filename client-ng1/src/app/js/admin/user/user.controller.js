(function () {

  'use strict';
  /*global componentHandler */

  angular.module('cesar-cfp').controller('AdminUserCtrl', function ($http) {
    'ngInject';

    var ctrl = this;

    if ($stateParams.id) {
      $http
        .get('app/cfp/proposal/' + $stateParams.id)
        .then(function (response) {
          ctrl.proposal = response.data;
          ctrl.check();
        });
    }

    ctrl.account = account;

    var i8nKeys = {
      categories: 'view.cfp.category.',
      status: 'view.cfp.status.',
      formats: 'view.cfp.format.',
      types: 'view.cfp.typeSession.',
      levels: 'view.cfp.level.',
      maxAttendees: 'view.cfp.nbattendees.'
    };

    $http.get('app/cfp/param').then(function (response) {
      response.data.forEach(function (elt) {
        ctrl[elt.key] = [];
        elt.value.forEach(function (param) {
          ctrl[elt.key][param] = i8nKeys[elt.key] + param;
        });
      });
    });

    ctrl.check = function () {
      if(ctrl.proposal.id){
        delete ctrl.warningMessage;
        delete ctrl.confirm;

        $http
          .post('app/cfp/proposal/check', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function (response) {
            ctrl.warningMessage = response.data;
            refresh();
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      }
    };

    ctrl.goback = function () {
      $state.go('cfp');
    };


    function refresh() {
      $timeout(function () {
        componentHandler.upgradeAllRegistered();
      }, 1000);
    }

  });
})();
