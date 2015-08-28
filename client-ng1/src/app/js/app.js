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

    $stateProvider

      .state('home', {
        url: '/home',
        templateUrl: 'views/home.html'
      })
      .state('speakers', stateMember('speakers', 'speaker'))
      .state('sponsors', stateMember('sponsors', 'sponsor'))
      .state('staff', stateMember('staff', 'staff'))
      .state('talks', stateSessions('talks', 'talk'))
      .state('lightningtalks', stateSessions('lightningtalks', 'lightningtalks'))

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
