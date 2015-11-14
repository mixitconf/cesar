(function () {

  'use strict';

  angular.module('cesar-account').controller('AccountCtrl', function ($http, $scope, $state, AuthenticationService) {
    'ngInject';

    var ctrl = this;
    var currentUser = AuthenticationService.currentUser();

    ctrl.account = {};

    console.log(AuthenticationService.currentUser())
    console.log($scope.userConnected);

    if(currentUser){
      var id = (angular.isUndefined(currentUser.login) || currentUser.login===null) ? currentUser.oauthId : currentUser.login;
      $http
        .get('app/account/' + id)
        .then(function (response) {
          ctrl.account = response.data;
        });
    }




    ctrl.updateAccount = function () {
      if (ctrl.account) {
        //$http
        //  .post('app/account/social', angular.copy(ctrl.credentials), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
        //  .then(function () {
        //    $state.go('useraccountcreated');
        //  })
        //  .catch(function (response) {
        //    if (response.data.type && response.data.type === 'EMAIL_EXIST'){
        //      ctrl.errorMessage = 'EMAIL_ALREADY_USED';
        //    }
        //    else {
        //      ctrl.errorMessage = 'UNDEFINED';
        //    }
        //  });
      }
    };

    ctrl.account = {
      login: 'dev-mind',
      email: 'guillaume@dev-mind.fr',
      firstname: 'Guillaume',
      lastname: 'EHRET',
      company: 'Dev-Mind',
      registeredAt: new Date(),
      shortDescription: 'lorem leptsum',
      longDescription: 'lorem leptsum',
      nbConsults: 1,
      interests: [],
      sharedLinks: [],
      publicProfile: true,
      provider: 'CESAR',
      defaultLanguage: 'en',
      valid: true

    }
  });

})();