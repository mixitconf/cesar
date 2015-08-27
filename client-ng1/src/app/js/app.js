(function () {

  'use strict';

  angular.module('cesar-utils', []);
  angular.module('cesar-menu', ['cesar-templates']);
  angular.module('cesar-home', ['cesar-templates']);
  angular.module('cesar-members', ['cesar-templates']);
  angular.module('cesar-sessions', ['cesar-templates']);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ui.router',
    'ngSanitize',
    'cesar-templates',
    'cesar-menu',
    'cesar-home',
    'cesar-members',
    'cesar-sessions',
    'cesar-services',
    'cesar-utils'
  ]);

  angular.module('cesar').config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

    $locationProvider.html5Mode();

    $urlRouterProvider.otherwise('/home');

    $stateProvider

      .state('home', {
        url: '/home',
        templateUrl: 'views/home.html'
      })
      .state('error', {
        url: '/error',
        templateUrl: 'views/error.html',
        params: {
          error: {}
        },
        controller: function ($scope, $stateParams) {
          $scope.error = $stateParams.error;
        }
      })
      .state('speakers', {
        url: '/speakers',
        controller: 'MembersCtrl',
        controllerAs: 'ctrl',
        templateUrl: 'views/members/speakers.html',
        data: {
          member: 'speaker'
        }
      })
      .state('sponsors', {
        url: '/sponsors',
        controller: 'MembersCtrl',
        controllerAs: 'ctrl',
        templateUrl: 'views/members/sponsors.html',
        data: {
          member: 'sponsor'
        }
      })
      .state('staff', {
        url: '/staff',
        controller: 'MembersCtrl',
        controllerAs: 'ctrl',
        templateUrl: 'views/members/staff.html',
        data: {
          member: 'staff'
        }
      })
      .state('member', {
        url: '/member/:type/:id/',
        templateUrl: 'views/members/member.html',
        controller: 'MemberCtrl',
        controllerAs: 'ctrl',
        resolve: {
          member: function (MemberService, $stateParams) {
            return MemberService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        }
      })
      .state('multimedia', {
        url: '/multimedia',
        templateUrl: 'views/info/multimedia.html'
      })
      .state('conduite', {
        url: '/conduite',
        templateUrl: 'views/info/conduite.html'
      })
      .state('faq', {
        url: '/faq',
        templateUrl: 'views/info/faq.html'
      })
      .state('venir', {
        url: '/venir',
        templateUrl: 'views/info/venir.html'
      });
  });

  angular.module('cesar').run(function ($rootScope, $state) {
    //Error are catched to redirect user on error page
    $rootScope.$on('$cesarError', function (event, response) {
      $state.go('error', {error: response});
    });
  });

})();
