(function () {

  'use strict';

  angular.module('cesar-utils', []);
  angular.module('cesar-articles', ['cesar-templates']);
  angular.module('cesar-menu', ['cesar-templates']);
  angular.module('cesar-home', ['cesar-templates']);
  angular.module('cesar-members', ['cesar-templates']);
  angular.module('cesar-sessions', ['cesar-templates']);
  angular.module('cesar-services', []);

  angular.module('cesar', [
    'ui.router',
    'ngSanitize',
    'cesar-templates',
    'cesar-articles',
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

    // State use to list members
    function stateMember(url, type){
      return {
        url: '/' + url,
        controller: function(members) {
          this.members = members;
        },
        controllerAs: 'ctrl',
        templateUrl: 'views/members/' + url + '.html',
        resolve: {
          members : function (MemberService){
            return  MemberService.getAll(type).then(function (response) {
              return response.data;
            });
          }
        }
      };
    }
    // State use to list sessions
    function stateSessions(url){
      return {
        url: '/' + url,
        controller: 'SessionsCtrl',
        controllerAs: 'ctrl',
        templateUrl: 'views/sessions/' + url + '.html',
        data:{
          type : url
        }
      };
    }
    // State old editions
    function stateOldEdition(url, year){
      return {
        url: '/' + url,
        controller: function(){
          this.year = year;
        },
        controllerAs: 'ctrl',
        templateUrl: 'views/sessions/oldedition.html'
      };
    }

    $stateProvider

      .state('home', {
        url: '/home',
        templateUrl: 'views/home.html'
      })
      .state('planning', {
        url: '/planning',
        templateUrl: 'views/sessions/planning.html'
      })
      .state('speakers', stateMember('speakers', 'speaker'))
      .state('sponsors', stateMember('sponsors', 'sponsor'))
      .state('staff', stateMember('staff', 'staff'))
      .state('talks', stateSessions('talks', 'talk'))
      .state('lightningtalks', stateSessions('lightningtalks', 'lightningtalks'))
      .state('mixit15', stateOldEdition('mixit15', 2015))
      .state('mixit14', stateOldEdition('mixit15', 2014))
      .state('mixit13', stateOldEdition('mixit15', 2013))
      .state('mixit12', stateOldEdition('mixit15', 2012))
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
      .state('articles', {
        url: '/articles',
        templateUrl: 'views/info/articles.html',
        controller: function(articles){
          this.articles = articles;
        },
        controllerAs: 'ctrl',
        resolve: {
          articles: function (ArticleService) {
            return ArticleService.getAll().then(function (response) {
              return response.data;
            });
          }
        }
      })
      .state('session', {
        url: '/session/:type/:id/',
        templateUrl: 'views/sessions/session.html',
        controller: 'SessionCtrl',
        controllerAs: 'ctrl',
        resolve: {
          session: function (SessionService, $stateParams) {
            return SessionService.getById($stateParams.id).then(function (response) {
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
