(function () {

  'use strict';

  angular.module('cesar-account').controller('CreateSocialAccountCtrl', function ($rootScope, $scope, $http, $state) {
    'ngInject';

    var ctrl = this;

    ctrl.credentials = {
      defaultLanguage : ($translate.proposedLanguage()===LANGUAGES.en ? 'en' : 'fr')
    };

    ctrl.createSocialAccount = function () {
      if (ctrl.credentials) {
        $http
          .put('app/account/social', angular.copy(ctrl.credentials), {ignoreErrorRedirection: 'ignoreErrorRedirection'})
          .then(function () {
            $state.go('doneaction', {title : 'view.account.creation.title', description : 'view.account.creation.confirmation'});
          })
          .catch(function (response) {
            if (response.data.type && response.data.type === 'EMAIL_EXIST'){
              ctrl.errorMessage = 'EMAIL_ALREADY_USED';
            }
            else {
              ctrl.errorMessage = 'UNDEFINED';
            }
          });
      }
    };

    var stateName = $state.current.name;
    var state;
    var onRouteChangeOff = $rootScope.$on('$stateChangeStart', routeChangeHandler);

    function routeChangeHandler(event, newState) {
      if (newState.name.indexOf(stateName) !== -1 && newState.url===state) {
        // we don't care about internal state change
        return;
      }
      state = newState.url;
      ctrl.errorMessage = 'CONFIRM_EXIT_FROM_CREATION_ACCOUNT';
      event.preventDefault();
    }

    // remove route event listener
    $scope.$on('$destroy', function () {
      onRouteChangeOff();
    });
  });

})();