(function () {
  'use strict';

  angular.module('cesar').config(function ($stateProvider, $urlRouterProvider, $locationProvider, USER_ROLES) {
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

    //Router definition
    $stateProvider

      //Home and error route
      .state('home', new State(USER_ROLES, 'home', 'views/home.html').build())

      .state('valid', new State(USER_ROLES, 'valid', 'views/home.html')
        .controller(
        /* @ngInject */
        function (AuthenticationService) {
          AuthenticationService.checkUser();
        })
        .build())

      .state('cerror', new State(USER_ROLES, 'cerror/{type}', 'views/error.html')
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
      .state('news', new State(USER_ROLES, 'article/:id/:title', 'views/info/news.html')
        .controller('ArticleCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('articles', new State(USER_ROLES, 'articles', 'views/info/articles.html')
        .controller(
        /* @ngInject */
        function (articles) {
          var ctrl = this;
          ctrl.articles = articles;
        })
        .resolve({articles: getAllArticles})
        .build())

      //Program
      .state('planning', new State(USER_ROLES, 'planning', 'views/sessions/planning.html').build())
      .state('talks', new State(USER_ROLES, 'talks', 'views/sessions/talks.html').controller('SessionsCtrl').data({type: 'talks'}).build())
      .state('lightningtalks', new State(USER_ROLES, 'lightningtalks', 'views/sessions/lightningtalks.html').controller('SessionsCtrl').data({type: 'lightningtalks'}).build())
      .state('mixit15', new State(USER_ROLES, 'mixit15', 'views/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2015}).build())
      .state('mixit14', new State(USER_ROLES, 'mixit14', 'views/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2014}).build())
      .state('mixit13', new State(USER_ROLES, 'mixit13', 'views/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2013}).build())
      .state('mixit12', new State(USER_ROLES, 'mixit12', 'views/sessions/talks.html').controller('SessionsClosedCtrl').data({year: 2012}).build())
      .state('session', new State(USER_ROLES, 'session/:id/:title', 'views/sessions/session.html').controller('SessionCtrl')
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
      .state('speakers', new State(USER_ROLES, 'speakers', 'views/members/speakers.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'speaker';
          }
        })
        .build())
      .state('sponsors', new State(USER_ROLES, 'sponsors', 'views/members/sponsors.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'sponsor';
          }
        })
        .build())
      .state('staff', new State(USER_ROLES, 'staff', 'views/members/staff.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers, type: function () {
            return 'staff';
          }
        })
        .build())
      .state('member', new State(USER_ROLES, 'member/:type/:id', 'views/members/member.html').controller('MemberCtrl')
        .resolve({
          /* @ngInject */
          member: function (MemberService, $stateParams) {
            return MemberService.getById($stateParams.id).then(function (response) {
              return response.data;
            });
          }
        })
        .build())

      //Infos
      .state('multimedia', new State(USER_ROLES, 'multimedia', 'views/info/multimedia.html').build())
      .state('conduite', new State(USER_ROLES, 'conduite', 'views/info/conduite.html').build())
      .state('faq', new State(USER_ROLES, 'faq', 'views/info/faq.html').build())
      .state('venir', new State(USER_ROLES, 'venir', 'views/info/venir.html').build())


      //Account
      .state('account', new State(USER_ROLES, 'account', 'views/account/account.html')
        .controller('AccountCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .resolve({
          /* @ngInject */
          account: function (AuthenticationService) {
            return AuthenticationService.currentUser().then(function(currentUser){
              return currentUser;
            });
          }
        })
        .build())
      .state('createaccount', new State(USER_ROLES, 'createaccount', 'views/account/create-user-account.html')
        .controller('CreateUserAccountCtrl')
        .build())
      .state('createaccountsocial', new State(USER_ROLES, 'createaccountsocial', 'views/account/create-social-account.html')
        .controller('CreateSocialAccountCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .build())


      //Security
      .state('logout', new State(USER_ROLES, 'logout', 'views/home.html').build())
      .state('authent', new State(USER_ROLES, 'authent', 'views/security/login.html').controller('LoginCtrl').build())
      .state('passwordlost', new State(USER_ROLES, 'passwordlost', 'views/security/password-lost.html').controller('PasswordLostCtrl').build())
      .state('passwordreinit', new State(USER_ROLES, 'passwordreinit', 'views/security/password-reinit.html')
        .controller('PasswordReinitCtrl')
        .roles([USER_ROLES.member, USER_ROLES.admin, USER_ROLES.speaker])
        .build())
      .state('doneaction', new State(USER_ROLES, 'doneaction', 'views/security/done-action.html')
        .controller('DoneActionCtrl')
        .params({
          title: null,
          description: null
        })
        .build())

      .state('monitor', new State(USER_ROLES, 'monitor', 'views/admin/monitoring.html')
        .controller('MonitoringCtrl')
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
