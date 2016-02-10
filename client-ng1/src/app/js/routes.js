(function () {
  'use strict';

  angular.module('cesar').config(function ($stateProvider, $urlRouterProvider, $locationProvider,  USER_ROLES) {
    'ngInject';

    $locationProvider.html5Mode({
      enabled: true,
      rewriteLinks: false
    });

    $urlRouterProvider.otherwise('/home');

    /* @ngInject */
    function getAllArticles(ArticleService) {
      return ArticleService.getAll().then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getAllMembers(MemberService, type) {
      return MemberService.getAll(type).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getAccount(AuthenticationService) {
      return AuthenticationService.currentUser().then(function (currentUser) {
        return currentUser;
      });
    }

    //Router definition
    $stateProvider

    //Home and error route
      .state('home', new State(USER_ROLES, 'home', 'js/home/home.html').controller('HomeCtrl').build())

      .state('valid', new State(USER_ROLES, 'valid', 'js/home/home.html')
        .controller(
          /* @ngInject */
          function (AuthenticationService) {
            AuthenticationService.checkUser();
          })
        .build())

      .state('cerror', new State(USER_ROLES, 'cerror/{type}', 'js/error/error.html')
        .params({
          error: {}
        })
        .controller(
          /* @ngInject */
          function ($scope, $stateParams) {
            $scope.error = $stateParams.error;
            $scope.type = $stateParams.type;
          })
        .build())

      //News
      .state('news', new State(USER_ROLES, 'article/:id/:title', 'js/article/article.html')
        .controller('ArticleCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('article', new State(USER_ROLES, 'article/:id', 'js/article/article.html')
        .controller('ArticleCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('articles', new State(USER_ROLES, 'articles', 'js/articles/articles.html')
        .controller('ArticlesCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('admplanning', new State(USER_ROLES, 'admplanning', 'js/admin/planning/planning.html')
        .controller('AdminPlanningCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admcfp', new State(USER_ROLES, 'admcfp', 'js/admin/cfp/cfp.html')
        .controller('AdminCfpCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admcfptalk', new State(USER_ROLES, 'admcfptalk/:id', 'js/admin/cfptalk/cfptalk.html')
        .controller('AdminCfpTalkCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admarticle', new State(USER_ROLES, 'admarticle/:id', 'js/admin/article/article.html')
        .controller('AdminArticleCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admarticles', new State(USER_ROLES, 'admarticles', 'js/admin/articles/articles.html')
        .controller('AdminArticlesCtrl')
        .resolve({
          /* @ngInject */
          articles: function (ArticleService) {
            return ArticleService.getAll(true).then(function (response) {
              return response.data;
            });
          }
        })
        .build())

      .state('admaccounts', new State(USER_ROLES, 'admaccounts', 'js/admin/accounts/accounts.html')
        .controller('AdminAccountsCtrl')
        .resolve({
          /* @ngInject */
          accounts: function ($http) {
            return $http.get('/app/account').then(function (response) {
              return response.data;
            });
          },
          account: getAccount
        })
        .build())

      //Program
      .state('planning', new State(USER_ROLES, 'planning', 'js/planning/planning.html').build())
      .state('talks', new State(USER_ROLES, 'talks', 'js/sessions/talks.html').controller('SessionsCtrl').data({type: 'talks'}).build())
      .state('lightningtalks', new State(USER_ROLES, 'lightningtalks', 'js/sessions/lightningtalks.html').controller('SessionsCtrl').data({type: 'lightningtalks'}).build())
      .state('mixit15', new State(USER_ROLES, 'mixit15', 'js/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2015}).build())
      .state('mixit14', new State(USER_ROLES, 'mixit14', 'js/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2014}).build())
      .state('mixit13', new State(USER_ROLES, 'mixit13', 'js/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2013}).build())
      .state('mixit12', new State(USER_ROLES, 'mixit12', 'js/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2012}).build())
      .state('session', new State(USER_ROLES, 'session/:id/:title', 'js/session/session.html').controller('SessionCtrl')
        .resolve({
          /* @ngInject */
          session: function (SessionService, $stateParams) {
            return SessionService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        })
        .build())
      .state('sessionwt', new State(USER_ROLES, 'session/:id', 'js/session/session.html').controller('SessionCtrl')
        .resolve({
          /* @ngInject */
          session: function (SessionService, $stateParams) {
            return SessionService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        })
        .build())

      //Participants
      .state('speakers', new State(USER_ROLES, 'speakers', 'js/members/speakers.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'speaker';
          }
        })
        .build())
      .state('sponsors', new State(USER_ROLES, 'sponsors', 'js/members/sponsors.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'sponsor';
          }
        })
        .build())
      .state('staff', new State(USER_ROLES, 'staff', 'js/members/staff.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'staff';
          }
        })
        .build())
      .state('member', new State(USER_ROLES, 'member/:type/:id?redirect', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          /* @ngInject */
          member: function (MemberService, $stateParams) {
            return MemberService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        })
        .build())
      .state('sponsor', new State(USER_ROLES, 'member/sponsor/:id', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          type: function () {
            return 'sponsor';
          },
          /* @ngInject */
          member: function (MemberService, $stateParams) {
            return MemberService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        })
        .build())
      .state('profile', new State(USER_ROLES, 'profile/:login', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          /* @ngInject */
          member: function (MemberService, $stateParams) {
            return MemberService.getByLogin($stateParams.login).then(function (response) {
              return response.data;
            });
          }
        })
        .build())
      //Infos
      .state('interest', new State(USER_ROLES, 'interest/:name', 'js/interest/interest.html').controller('InterestCtrl').build())
      .state('multimedia', new State(USER_ROLES, 'multimedia', 'js/multimedia/multimedia.html').build())
      .state('conduite', new State(USER_ROLES, 'conduite', 'js/conduite/conduite.html').build())
      .state('faq', new State(USER_ROLES, 'faq', 'js/faq/faq.html').build())
      .state('venir', new State(USER_ROLES, 'venir', 'js/venir/venir.html').build())

      .state('cfp', new State(USER_ROLES, 'cfp', 'js/cfp/cfp.html')
        .controller('CfpCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .resolve({account: getAccount})
        .build())

      .state('cfptalk', new State(USER_ROLES, 'cfptalk/:id', 'js/cfptalk/cfptalk.html')
        .controller('CfpTalkCtrl')
        .resolve({account: getAccount})
        .build())

      //Account
      .state('account', new State(USER_ROLES, 'account?redirect', 'js/account/account.html')
        .controller('AccountCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .resolve({account: getAccount})
        .build())
      .state('createaccount', new State(USER_ROLES, 'createaccount', 'js/create-user-account/create-user-account.html')
        .controller('CreateUserAccountCtrl')
        .build())
      .state('createaccountsocial', new State(USER_ROLES, 'createaccountsocial', 'js/create-social-account/create-social-account.html')
        .controller('CreateSocialAccountCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .build())


      //Security
      .state('logout', new State(USER_ROLES, 'logout', 'js/home/home.html').build())
      .state('authent', new State(USER_ROLES, 'authent?redirect', 'js/login/login.html').controller('LoginCtrl').build())
      .state('passwordlost', new State(USER_ROLES, 'passwordlost', 'js/password-lost/password-lost.html').controller('PasswordLostCtrl').build())
      .state('passwordreinit', new State(USER_ROLES, 'passwordreinit', 'js/password-reinit/password-reinit.html')
        .controller('PasswordReinitCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .build())
      .state('doneaction', new State(USER_ROLES, 'doneaction', 'js/done-action/done-action.html')
        .controller('DoneActionCtrl')
        .params({
          title: null,
          description: null
        })
        .build())

      .state('monitor', new State(USER_ROLES, 'monitor', 'js/monitoring/monitoring.html')
        .controller('MonitoringCtrl')
        .roles([USER_ROLES.admin])
        .build())

      .state('cache', new State(USER_ROLES, 'cache', 'js/cache/cache.html')
        .controller('CacheCtrl')
        .roles([USER_ROLES.admin])
        .build());

  });

  //Create an object to use a fluent API to define routes
  function State(USER_ROLES, url, view) {
    this.state = {
      url: '/' + url,
      templateUrl: view,
      authorizedRoles: [USER_ROLES.all]
    };
  }

  State.prototype.roles = function (rights) {
    this.state.authorizedRoles = rights;
    return this;
  };
  State.prototype.resolve = function (resolve) {
    this.state.resolve = resolve;
    return this;
  };
  State.prototype.params = function (params) {
    this.state.params = params;
    return this;
  };
  State.prototype.data = function (data) {
    this.state.data = data;
    return this;
  };
  State.prototype.controller = function (controller) {
    this.state.controller = controller;
    this.state.controllerAs = 'ctrl';
    return this;
  };
  State.prototype.build = function () {
    return this.state;
  };
})();
