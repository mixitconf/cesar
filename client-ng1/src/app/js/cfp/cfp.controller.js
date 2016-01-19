(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').controller('CfpCtrl', function ($rootScope, $http, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
      return;
    }

    $http.get('app/cfp/param/category').then(
      function(response){
        ctrl.categories = response.data;
      }
    );

    ctrl.checkProfile = function(type){

      switch(type){
        case 'id':
          return !!account.firstname && !!account.lastname;
        case 'email':
          return !!account.email;
        case 'company':
          return !!account.member.company;
        case 'summary':
          return !!account.member.shortDescription;
        case 'description':
          return !!account.member.longDescription;
        case 'link':
          return account.member.sharedLinks.length>0;
        default:
          return ctrl.checkProfile('id') && ctrl.checkProfile('email') &&  ctrl.checkProfile('company') && ctrl.checkProfile('summary') && ctrl.checkProfile('description') && ctrl.checkProfile('link');
      }
    };


    ctrl.refresh = function(){
      $http.get('app/cfp/proposal/mine').then(
        function(response){
          ctrl.proposals = response.data;
        }
      );
    };

    ctrl.account = account;
    ctrl.profileOk= ctrl.checkProfile();
    ctrl.refresh();
  });
})();
