(function () {

  'use strict';

  angular.module('cesar-account').controller('AccountCtrl', function ($rootScope, $http, $state,$stateParams, $translate, $filter, account, LANGUAGES) {
    'ngInject';

    var ctrl = this;
    ctrl.account = account;

    ctrl.getDate = function(date){
      return $filter('date')(date, 'dd/MM/yyyy');
    };

    if(ctrl.account){
      ctrl.updateAccount = function () {
        $http
          .put('app/account', angular.copy(ctrl.account), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            if(ctrl.account.defaultLanguage){
              $translate.use((ctrl.account.defaultLanguage === 'en') ? LANGUAGES.en : LANGUAGES.fr);
            }
            $rootScope.$broadcast('event:auth-changed');
            if($stateParams.redirect){
              $state.go($stateParams.redirect);
            }
            else{
              $state.go('home');
            }
          })
          .catch(function (response) {
            if (response.data.type && response.data.type === 'USER_NOT_FOUND'){
              ctrl.errorMessage = 'USER_NOT_FOUND';
            }
            else if (response.data.type && response.data.type==='EMAIL_EXIST'){
              ctrl.errorMessage = 'EMAIL_ALREADY_USED';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      };

      if(!account.member.sharedLinks){
        account.member.sharedLinks= [];
      }
      for(var i=account.member.sharedLinks.length ; i<4 ; i++){
        account.member.sharedLinks.push({name : ''});
      }
    }

    ctrl.addInterest = function(value){
      if(value){
        var canPush;
        if(!ctrl.account.member.interests){
          canPush = true;
        }
        else{
          canPush = ctrl.account.member.interests.filter(function(elt){
            return elt.name === value;
          }).length===0;
        }
        if(canPush){
          ctrl.account.member.interests.push({name : value});
        }

      }
    };

    ctrl.removeInterest = function(value){
      ctrl.account.member.interests.splice(ctrl.account.member.interests.indexOf(value),1);
    };

  });

})();