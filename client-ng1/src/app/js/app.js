(function() {

  'use strict';

  angular.module('cesar-menu', []);
  angular.module('cesar-home', []);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ngRoute',
    'cesar.templates',
    'cesar-menu',
    'cesar-home',
    'cesar-services'
  ]);

  angular.module('cesar').config(function($routeProvider){

    $routeProvider.when('/', {
      templateUrl: 'views/home.html',
      controller: 'HomeCtrl',
      controllerAs: 'home'
    });

    $routeProvider.otherwise('/');
  });
})();
