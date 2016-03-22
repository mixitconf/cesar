(function () {

  'use strict';
  /*global moment */

  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-8046496-2', 'auto');
  ga('send', 'pageview');


  angular.module('cesar-utils', ['ngSanitize']);
  angular.module('cesar-articles', ['cesar-templates']);
  angular.module('cesar-menu', ['cesar-templates']);
  angular.module('cesar-home', ['cesar-templates']);
  angular.module('cesar-cfp', ['cesar-templates']);
  angular.module('cesar-members', ['cesar-templates', 'cesar-utils']);
  angular.module('cesar-sessions', ['cesar-templates', 'cesar-utils']);
  angular.module('cesar-services', []);
  angular.module('cesar-planning', []);
  angular.module('cesar-security', ['ngCookies']);
  angular.module('cesar-account', []);

  angular.module('cesar', [
    'ui.router',
    'ngSanitize',
    'ngCookies',
    'cesar-account',
    'cesar-cfp',
    'cesar-templates',
    'cesar-articles',
    'cesar-menu',
    'cesar-home',
    'cesar-members',
    'cesar-planning',
    'cesar-sessions',
    'cesar-services',
    'cesar-utils',
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
      var mixitStart = moment($rootScope.cesar.day1).hours('0');
      var mixitEnd = moment($rootScope.cesar.day2).hours('19');

      $rootScope.cesar.voteIsOpen = moment().isAfter(mixitStart) && moment().isBefore(mixitEnd) && $rootScope.cesar.current==='2016';
    });
  });
})();


