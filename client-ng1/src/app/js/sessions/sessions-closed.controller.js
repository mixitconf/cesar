(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsClosedCtrl', function ($state, SessionService) {
    var ctrl = this;
    var year = $state.current.data.year;

    ctrl.year = year;

    SessionService.getAllByYear(year).then(function(response){
      ctrl.sessions = response.data;
    });

  });
})();