(function() {

  'use strict';

  angular.module('cesar-menu', []);
  angular.module('cesar-home', []);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ui.router',
    'cesar.templates',
    'cesar-menu',
    'cesar-home',
    'cesar-services'
  ]);

  angular.module('cesar').config(function($stateProvider, $urlRouterProvider, $locationProvider){

    $locationProvider.html5Mode();

    $urlRouterProvider.otherwise('/home');

    $stateProvider
      .state('home', {
        url: '/home',
        controller: 'HomeCtrl',
        controllerAs: 'home',
        templateUrl: 'views/home.html'
      })

  });
})();
