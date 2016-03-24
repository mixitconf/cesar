(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($state, sessions, sponsors) {
    'ngInject';

    var ctrl = this;

    ctrl.year = $state.current.data.year;
    ctrl.sessions = sessions;
    ctrl.sponsors = sponsors;

  });
})();