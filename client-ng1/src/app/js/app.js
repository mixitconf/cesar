(function () {

  'use strict';

  angular.module('cesar-utils', ['ngSanitize']);
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

    $locationProvider.html5Mode(true);

    $urlRouterProvider.otherwise('/home');

    // State use to list members
    function stateMember(url, type) {
      return {
        url: '/' + url,
        resolve: {
          members: function (MemberService) {
            return MemberService.getAll(type).then(function (response) {
              return response.data;
            });
          }
        },
        views: {
          main: {
            templateUrl: 'views/members/' + url + '.html',
            controller: function (MemberService) {
              var ctrl = this;
              MemberService.getAll(type).then(function (response) {
                ctrl.members = response.data;
              });
            },
            controllerAs: 'ctrl'
          }
        }
      };
    }

    // State use to list sessions
    function stateSessions(url) {
      return {
        url: '/' + url,
        views: {
          main: {
            templateUrl: 'views/sessions/' + url + '.html',
            controller: 'SessionsCtrl',
            controllerAs: 'ctrl'
          }
        },
        data: {
          type: url
        }
      };
    }

    // State old editions
    function stateOldEdition(url, year) {
      return {
        url: '/' + url,
        views: {
          main: {
            templateUrl: 'views/sessions/oldedition.html',
            controller: function () {
              this.year = year;
            },
            controllerAs: 'ctrl'
          }
        }
      };
    }

    function stateSimplePage(url, template) {
      return {
        url: '/' + url,
        views: {
          main: {
            templateUrl: template
          }
        }
      };
    }

    //Router definition
    $stateProvider
      .state('error', {
        url: '/error',
        params: {
          error: {}
        },
        views: {
          main: {
            templateUrl: 'views/error.html',
            controller: function ($scope, $stateParams) {
              $scope.error = $stateParams.error;
            }
          }
        }
      })

      //Home Page
      .state('home', stateSimplePage('home','views/home.html'))

      //News
      .state('news', {
        url: '/article/:id',
        resolve: {
          articles: function (ArticleService) {
            return ArticleService.getAll().then(function (response) {
              return response.data;
            });
          }
        },
        views: {
          main: {
            templateUrl: 'views/info/news.html',
            controller: 'ArticleCtrl',
            controllerAs: 'ctrl'
          }
        }
      })
      //News
      .state('articles', {
        url: '/articles',
        resolve: {
          articles: function (ArticleService) {
            return ArticleService.getAll().then(function (response) {
              return response.data;
            });
          }
        },
        views: {
          main: {
            templateUrl: 'views/info/articles.html',
            controller: 'ArticleCtrl',
            controllerAs: 'ctrl'
          }
        }
      })

      //Program
      .state('planning', stateSimplePage('planning','views/sessions/planning.html'))
      .state('talks', stateSessions('talks', 'talk'))
      .state('lightningtalks', stateSessions('lightningtalks', 'lightningtalks'))
      .state('session', {
        url: '/session/:type/:id',
        resolve: {
          session: function (SessionService, $stateParams) {
            return SessionService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        },
        views: {
          main: {
            templateUrl: 'views/sessions/session.html',
            controller: 'SessionCtrl',
            controllerAs: 'ctrl'
          }
        }
      })

      //Participants
      .state('speakers', stateMember('speakers', 'speaker'))
      .state('sponsors', stateMember('sponsors', 'sponsor'))
      .state('staff', stateMember('staff', 'staff'))
      .state('member', {
        url: '/member/:type/:id',
        resolve: {
          member: function (MemberService, $stateParams) {
            return MemberService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        },
        views: {
          main: {
            templateUrl: 'views/members/member.html',
            controller: 'MemberCtrl',
            controllerAs: 'ctrl'
          }
        }
      })

      //Infos
      .state('multimedia', stateSimplePage('multimedia','views/info/multimedia.html'))
      .state('conduite', stateSimplePage('conduite','views/info/conduite.html'))
      .state('faq', stateSimplePage('faq','views/info/faq.html'))
      .state('venir', stateSimplePage('venir','views/info/venir.html'))
      .state('mixit15', stateOldEdition('mixit15', 2015))
      .state('mixit14', stateOldEdition('mixit15', 2014))
      .state('mixit13', stateOldEdition('mixit15', 2013))
      .state('mixit12', stateOldEdition('mixit15', 2012));
  });

  angular.module('cesar').run(function ($rootScope, $state) {
    //Error are catched to redirect user on error page
    $rootScope.$on('$cesarError', function (event, response) {
      $state.go('error', {error: response});
    });
  });

})();
