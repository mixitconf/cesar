(function () {

  'use strict';

  angular.module('cesar-utils', ['ngSanitize']);
  angular.module('cesar-articles', ['cesar-templates']);
  angular.module('cesar-menu', ['cesar-templates']);
  angular.module('cesar-home', ['cesar-templates']);
  angular.module('cesar-members', ['cesar-templates', 'cesar-utils']);
  angular.module('cesar-sessions', ['cesar-templates', 'cesar-utils']);
  angular.module('cesar-services', []);
  angular.module('cesar-security', ['ngCookies']);
  angular.module('cesar-account', []);

  angular.module('cesar', [
    'ui.router',
    'ngSanitize',
    'ngCookies',
    'cesar-account',
    'cesar-templates',
    'cesar-articles',
    'cesar-menu',
    'cesar-home',
    'cesar-members',
    'cesar-sessions',
    'cesar-services',
    'cesar-utils',
    'hc.marked',
    'cesar-security',
    'pascalprecht.translate'
  ]);

  angular.module('cesar').config(function($httpProvider) {
    $httpProvider.interceptors.push('cesarErrorInterceptor', 'cesarSpinnerInterceptor');
  });

  /**
   * On startup we read info on application
   */
  angular.module('cesar').run(function($http, $rootScope) {
    $http.get('/api/cesar').then(function(response){
      $rootScope.cesar = response.data;
    });
  });
})();


