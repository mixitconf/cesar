(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (account, sessions, sponsors) {
    'ngInject';

    var ctrl = this;
    ctrl.userConnected = !!account;
    ctrl.sponsors = sponsors;
    ctrl.sessions = sessions;
  });
})();