(function () {

  'use strict';

  angular.module('cesar-sessions').controller('LightningtalkCtrl', function (lightning, type, $state, $http, Util, MemberService, SessionService, account) {
    'ngInject';

    var ctrl = this;
    ctrl.proposal = lightning ? lightning : { _links : []};

    ctrl.type = type;

    if(ctrl.type && !account){
      //In edition mode we need to be autheticated
      $state.go('authent');
    }
    //default language is fr
    if(!ctrl.proposal.lang){
      ctrl.proposal.lang = 'fr';
    }

    var speakers = Array.isArray(ctrl.proposal._links.speaker) ? ctrl.proposal._links.speaker : [ctrl.proposal._links.speaker];
    ctrl.proposal.speakers = [];
    speakers.forEach(function (speaker) {
      if(speaker){
        MemberService.getById(Util.extractId(speaker.href)).then(function (response) {
          ctrl.proposal.speakers.push(response.data);
        });
      }
    });

    ctrl.save = function (spinner) {
      if(spinner==='off'){
        delete ctrl.errorMessage;
        delete ctrl.confirm;
        var proposal = angular.copy(ctrl.proposal);
        delete proposal._links;

        $http
          .post('app/session', angular.copy(proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('lightnings');
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });

      }
    };

    ctrl.delete = function () {
      delete ctrl.errorMessage;
      if (ctrl.confirm && ctrl.confirm.display && ctrl.confirm.message) {

        if (ctrl.confirm.message.toLowerCase() === account.firstname.toLowerCase()) {
          $http
            .delete('app/session/' + ctrl.proposal.idSession, {ignoreErrorRedirection: 'ignoreErrorRedirection'})
            .then(function () {
              $state.go('lightnings');
            })
            .catch(function () {
              ctrl.errorMessage = 'UNDEFINED';
            });
        }
        else {
          ctrl.errorMessage = 'CONFIRMDELETE';
        }
      }
      else {
        ctrl.confirm = {
          display: true
        };
      }
    };
  });
})();