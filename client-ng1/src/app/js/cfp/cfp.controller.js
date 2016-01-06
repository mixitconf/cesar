(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-articles').controller('CfpCtrl', function ($rootScope, $http, account) {
    'ngInject';

    var ctrl = this;

    if(!account){
      $rootScope.$broadcast('event:auth-loginRequired');
    }

    $http.get('app/cfp/param/category').then(
      function(response){
        ctrl.categories = response.data;
      }
    );

    ctrl.checkProfile = function(type){
      switch(type){
        case 'id':
          return !account.member.firstname || !account.member.lastname;
        case 'email':
          return !account.member.email;
        case 'company':
          return !account.member.company;
        case 'summary':
          return !account.member.shortDescription;
        case 'description':
          return !account.member.longDescription;
        case 'link':
          return account.member.sharedLinks.length<1;
        default:
          return ctrl.checkProfile('id') && ctrl.checkProfile('email') &&  ctrl.checkProfile('company') && ctrl.checkProfile('summary') && ctrl.checkProfile('description') && ctrl.checkProfile('link');
      }
    };


    ctrl.refresh = function(){
      $http.get('app/cfp/proposal').then(
        function(response){
          ctrl.proposals = response.data;
        }
      );
    };

    ctrl.account = account;
    ctrl.refresh();
  });
})();
