(function () {

  'use strict';

  angular.module('cesar-menu', []);
  angular.module('cesar-home', []);
  angular.module('cesar-members', []);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ui.router',
    'cesar.templates',
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
        controller: 'HomeCtrl',
        controllerAs: 'home',
        templateUrl: 'views/home.html'
      })
      .state('speakers', {
        url: '/speakers',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/speakers.html'
      })
      .state('sponsors', {
        url: '/sponsors',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/sponsors.html'
      })
      .state('staff', {
        url: '/staff',
        controller: 'MemberCtrl',
        controllerAs: 'member',
        templateUrl: 'views/members/staff.html'
      });
  });
})();
