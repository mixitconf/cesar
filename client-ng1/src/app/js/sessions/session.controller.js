(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionCtrl', function (session, $stateParams, Util, MemberService) {
    var ctrl = this;

    ctrl.session = session;
    ctrl.session.speakers = [];
    ctrl.type = $stateParams.type;

    var speakers = Array.isArray(session._links.speaker) ? session._links.speaker : [session._links.speaker];

    speakers.forEach(function(speaker){
      MemberService.getById(Util.extractId(speaker.href)).then(function(response){
        ctrl.session.speakers.push(response.data);
      });
    });

  });
})();