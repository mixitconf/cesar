(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalkCtrl', function (lightning, type, $state, $http, Util, MemberService, SessionService, account) {
    'ngInject';

    var ctrl = this;
    ctrl.proposal = lightning;

    ctrl.type = type;

    if(ctrl.type && !account){
      //In edition mode we need to be autheticated
      $state.go('authent');
    }
    //default language is fr
    if(!ctrl.proposal.lang){
      ctrl.proposal.lang = 'fr';
    }

    var speakers = Array.isArray(lightning._links.speaker) ? lightning._links.speaker : [lightning._links.speaker];
    ctrl.proposal.speakers = [];
    speakers.forEach(function (speaker) {
      MemberService.getById(Util.extractId(speaker.href)).then(function (response) {
        ctrl.proposal.speakers.push(response.data);
      });
    });

    ctrl.save = function (spinner) {
      if(spinner==='off'){
        delete ctrl.errorMessage;
        delete ctrl.confirm;
        $http
          .post('app/session', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function (response) {
            ctrl.proposal = response.data;
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });

      }
    };
  });
})();