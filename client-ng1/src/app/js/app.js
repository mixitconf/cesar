(function () {

  'use strict';

  angular.module('cesar-menu', ['cesar-templates']);
  angular.module('cesar-home', ['cesar-templates']);
  angular.module('cesar-members', ['cesar-templates']);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ui.router',
    'cesar-templates',
    'cesar-menu',
    'cesar-home',
    'cesar-members',
    'cesar-services'
  ]);

  angular.module('cesar').config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

    $locationProvider.html5Mode();

    $urlRouterProvider.otherwise('/home');

    $stateProvider
      .state('home', {
        url: '/home',
        templateUrl: 'views/home.html'
      })
      .state('speakers', {
        url: '/speakers',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/speakers.html',
        data: {
          member : 'speaker'
        }
      })
      .state('sponsors', {
        url: '/sponsors',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/sponsors.html',
        data: {
          member : 'sponsor'
        }
      })
      .state('staff', {
        url: '/staff',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/staff.html',
        data: {
          member : 'staff'
        }
      });
  });
})();
