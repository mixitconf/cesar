(function () {

  'use strict';
  /*global componentHandler */

  angular.module('cesar-cfp').controller('CfpTalkCtrl', function ($http, $state, $stateParams,$timeout, CfpService, account) {
    'ngInject';

    var ctrl = this;

    if($stateParams.id){
      $http
        .get('app/cfp/proposal/' + $stateParams.id)
        .then(function (response) {
          ctrl.proposal = response.data;
        });
    }

    if(!ctrl.proposal){
      ctrl.proposal = {
        interests : []
      };
    }
    ctrl.account = account;

    CfpService.getCategories().then(function(categories){
      ctrl.categories= categories;
    });
    CfpService.getFormats().then(function(formats){
      ctrl.formats= formats;
    });
    CfpService.getSessionTypes().then(function(types){
      ctrl.types= types;
    });
    CfpService.getLevels().then(function(levels){
      ctrl.levels= levels;
    });
    CfpService.getMaxAttendees().then(function(maxAttendees){
      ctrl.maxAttendees= maxAttendees;
    });

    ctrl.addInterest = function(value){
      if(value){
        var canPush;
        if(!ctrl.proposal.interests){
          canPush = true;
        }
        else{
          canPush = ctrl.proposal.interests.filter(function(elt){
              return elt.name === value;
            }).length===0;
        }
        if(canPush){
          ctrl.proposal.interests.push({name : value});
        }

      }
    };

    ctrl.addSpeaker = function(value){
      if(value){
        var canPush;
        if(!ctrl.proposal.speakers){
          canPush = true;
        }
        else{
          canPush = ctrl.proposal.speakers.filter(function(elt){
              return elt.id === value.value;
            }).length===0;
        }
        if(canPush){
          ctrl.proposal.speakers.push({id : value.value, name : value.key});
        }

      }
    };

    ctrl.removeSpeaker = function(value){
      ctrl.proposal.speakers.splice(ctrl.proposal.speakers.indexOf(value),1);
    };

    ctrl.removeInterest = function(value){
      ctrl.proposal.interests.splice(ctrl.proposal.interests.indexOf(value),1);
    };

    ctrl.save = function () {
      delete ctrl.errorMessage;
      $http
        .post('app/cfp/proposal', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
        .then(function () {
          $state.go('cfp');
        })
        .catch(function () {
          ctrl.errorMessage = 'UNDEFINED';
        });
    };

    $timeout(function () {
      componentHandler.upgradeAllRegistered();
    },1000);
  });
})();
