(function () {

  'use strict';

  angular.module('cesar-cfp').controller('CfpTalkCtrl', function ($http, $state, CfpService) {
    'ngInject';

    var ctrl = this;

    if(!ctrl.proposal){
      ctrl.proposal = {
        interests : []
      };
    }

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

    ctrl.removeInterest = function(value){
      ctrl.proposal.interests.splice(ctrl.proposal.interests.indexOf(value),1);
    };

    ctrl.save = function () {
      $http
        .post('app/cfp/proposal', angular.copy(ctrl.proposal), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
        .then(function () {
          $state.go('cfp');
        })
        .catch(function () {
          ctrl.errorMessage = 'UNDEFINED';
        });
    };

  });
})();
