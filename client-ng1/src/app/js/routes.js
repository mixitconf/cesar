(function () {
  'use strict';

  angular.module('cesar').config(function ($stateProvider, $urlRouterProvider, $locationProvider, USER_ROLES) {
    'ngInject';

    $locationProvider.html5Mode({
      enabled: true,
      rewriteLinks: false
    });

    $urlRouterProvider.otherwise('/');

    /* @ngInject */
    function getAllAdminArticles(ArticleService) {
      return ArticleService.getAll(true).then(function (response) {
        return response.data;
      });
    }

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

    /* @ngInject */
    function getAllAccount($http) {
      return $http.get('/app/account').then(function (response) {
        return response.data;
      });
    }

    function _getAllSessions(SessionService, year) {
      return SessionService.getAllByYear(year).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getAllSessions(SessionService) {
      return _getAllSessions(SessionService, 2016);
    }
    /* @ngInject */
    function getAll2015Sessions(SessionService) {
      return _getAllSessions(SessionService, 2015);
    }
    /* @ngInject */
    function getAll2014Sessions(SessionService) {
      return _getAllSessions(SessionService, 2014);
    }
    /* @ngInject */
    function getAll2013Sessions(SessionService) {
      return _getAllSessions(SessionService, 2013);
    }
    /* @ngInject */
    function getAll2012Sessions(SessionService) {
      return _getAllSessions(SessionService, 2012);
    }

    function _getSponsors(MemberService, year) {
      return MemberService.getAll('sponsor', year).then(function (response) {
        return response.data;
      });
    }
    /* @ngInject */
    function getAllSponsors(MemberService) {
      return _getSponsors(MemberService, 2016);
    }
    /* @ngInject */
    function getAll2015Sponsors(MemberService) {
      return _getSponsors(MemberService, 2015);
    }
    /* @ngInject */
    function getAll2014Sponsors(MemberService) {
      return _getSponsors(MemberService, 2014);
    }
    /* @ngInject */
    function getAll2013Sponsors(MemberService) {
      return _getSponsors(MemberService, 2013);
    }
    /* @ngInject */
    function getAll2012Sponsors(MemberService) {
      return _getSponsors(MemberService, 2012);
    }

    /* @ngInject */
    function getLightning(SessionService, $stateParams) {
      return SessionService.getById($stateParams.id).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getMyLigthning($http, $q, account){
      if(!account){
        return $q.when([]);
      }
      else{
        return $http.get('app/session/mylightnings').then(function(response){
          return response.data;
        });
      }
    }

    /* @ngInject */
    function getLightningVotes($http, $q, account){
      if(!account){
        return $q.when([]);
      }
      else{
        return $http.get('app/session/lightnings/votes').then(function(response){
          return response.data;
        });
      }
    }

    /* @ngInject */
    function getMyFavorites($http, $q, account){
      if(!account){
        return $q.when([]);
      }
      else{
        return $http.get('app/favorite').then(function(response){
          return response.data;
        });
      }
    }

    /* @ngInject */
    function getSessionsByInterest($http, $stateParams){
      return $http.get('/api/session/interest/' + $stateParams.name).then(function(response){
        return response.data;
      });
    }

    /* @ngInject */
    function getMembersByInterest($http, $stateParams){
      return $http.get('/api/member/interest/' + $stateParams.name).then(function(response){
        return response.data;
      });
    }

    function getEditionMode(){
      return 'edition';
    }

    function getCreationMode(){
      return 'creation';
    }

    function getTypeMemberStaff(){
      return 'staff';
    }

    function getTypeMemberSpeaker(){
      return 'speaker';
    }

    function getTypeMemberSponsor(){
      return 'sponsor';
    }

    /* @ngInject */
    function getSession(SessionService, $stateParams) {
      return SessionService.getById($stateParams.id).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getMember(MemberService, $stateParams) {
      return MemberService.getById($stateParams.id).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getMemberByLogin(MemberService, $stateParams) {
      return MemberService.getByLogin($stateParams.login).then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getSessionRanking($http) {
      return $http.get('/api/ranking').then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getRooms(PlanningService) {
      return  PlanningService.getRoom().then(function (response) {
        return response.data;
      });
    }

    /* @ngInject */
    function getTransversalSlots(PlanningService) {
      return PlanningService.getTransversalSlots().then(function (response) {
        return response.data;
      });
    }

    //Router definition
    $stateProvider

    //Home and error route
      .state('start', new State(USER_ROLES, '', 'js/home/home.html')
        .controller('HomeCtrl')
        .build())

      .state('home', new State(USER_ROLES, 'home', 'js/home/home.html')
        .controller('HomeCtrl')
        .build())

      .state('valid', new State(USER_ROLES, 'valid', 'js/home/home.html')
        .controller(
          /* @ngInject */
          function (AuthenticationService) {
            AuthenticationService.checkUser();
          })
        .build())

      .state('cerror2', new State(USER_ROLES, 'cerror', 'js/error/error.html')
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
      .state('article', new State(USER_ROLES, 'article?search', 'js/articles/articles.html')
        .controller('ArticlesCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('article_zoom1', new State(USER_ROLES, 'article/:id', 'js/article/article.html')
        .controller('ArticleCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('article_zoom2', new State(USER_ROLES, 'article/:id/:title', 'js/article/article.html')
        .controller('ArticleCtrl')
        .resolve({articles: getAllArticles})
        .build())

      .state('admplanning', new State(USER_ROLES, 'admplanning', 'js/admin/planning/planning.html')
        .controller('AdminPlanningCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admcfp', new State(USER_ROLES, 'admcfp?search', 'js/admin/cfp/cfp-admin.html')
        .controller('AdminCfpCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admcfptalk', new State(USER_ROLES, 'admcfptalk/:id', 'js/admin/cfptalk/cfptalk.html')
        .controller('AdminCfpTalkCtrl')
        .resolve({account: getAccount})
        .build())

      .state('statistics', new State(USER_ROLES, 'statistics?search', 'js/statistics/statistics.html')
        .controller('StatisticsCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admarticle', new State(USER_ROLES, 'admarticle/:id', 'js/admin/article/article.html')
        .controller('AdminArticleCtrl')
        .resolve({account: getAccount})
        .build())

      .state('admarticles', new State(USER_ROLES, 'admarticles?search', 'js/admin/articles/articles.html')
        .controller('ArticlesCtrl')
        .resolve({articles: getAllAdminArticles})
        .build())

      .state('admaccounts', new State(USER_ROLES, 'admaccounts?search', 'js/admin/accounts/accounts.html')
        .controller('AdminAccountsCtrl')
        .resolve({
          accounts: getAllAccount,
          account: getAccount
        })
        .build())

      .state('planning', new State(USER_ROLES, 'planning?format&room&search&mode', 'js/planning/planning.html')
        .controller('PlanningCtrl')
        .resolve({
          account: getAccount,
          rooms : getRooms,
          transversalSlots : getTransversalSlots,
          sessions : getAllSessions,
          favorites : getMyFavorites
        })
        .build())

      .state('facilities', new State(USER_ROLES, 'facilities', 'js/facilities/facilities.html')
        .controller('HomeCtrl')
        .build())

      .state('mixteen', new State(USER_ROLES, 'mixteen', 'js/mixteen/mixteen.html')
        .controller('MixteenCtrl')
        .build())

      .state('mixit15', new State(USER_ROLES, 'mixit15?search', 'js/sessions/talks.html')
        .controller('SessionsClosedCtrl')
        .data({year: 2015})
        .resolve({
          sessions : getAll2015Sessions,
          sponsors: getAll2015Sponsors
        })
        .build())

      .state('mixit14', new State(USER_ROLES, 'mixit14?search', 'js/sessions/talks.html')
        .controller('SessionsClosedCtrl')
        .data({year: 2014})
        .resolve({
          sessions : getAll2014Sessions,
          sponsors: getAll2014Sponsors
        })
        .build())

      .state('mixit13', new State(USER_ROLES, 'mixit13?search', 'js/sessions/talks.html')
        .controller('SessionsClosedCtrl')
        .data({year: 2013})
        .resolve({
          sessions : getAll2013Sessions,
          sponsors: getAll2013Sponsors
        })
        .build())

      .state('mixit12', new State(USER_ROLES, 'mixit12?search', 'js/sessions/talks.html')
        .controller('SessionsClosedCtrl')
        .data({year: 2012})
        .resolve({
          sessions : getAll2012Sessions,
          sponsors: getAll2012Sponsors
        })
        .build())

      .state('lightnings', new State(USER_ROLES, 'lightnings?search', 'js/lt/lightningtalks.html')
        .controller('LightningtalksCtrl')
        .resolve({
          account: getAccount,
          myvotes: getLightningVotes,
          mylighnings: getMyLigthning
        })
        .build())

      .state('lightning-create', new State(USER_ROLES, 'lightning', 'js/lt/lightningtalk.html').controller('LightningtalkCtrl')
        .resolve({
          lightning: angular.noop,
          type: getCreationMode,
          account: getAccount
        })
        .build())
      .state('lightning-edition', new State(USER_ROLES, 'lightning/:id/edit', 'js/lt/lightningtalk.html').controller('LightningtalkCtrl')
        .resolve({
          /* @ngInject */
          lightning: getLightning,
          type: getEditionMode,
          account: getAccount
        })
        .build())
      .state('lightning', new State(USER_ROLES, 'lightning/:id', 'js/lt/lightningtalk.html').controller('LightningtalkCtrl')
        .resolve({
          lightning: getLightning,
          account : angular.noop,
          type: angular.noop
        })
        .build())

      .state('talks', new State(USER_ROLES, 'talks?search', 'js/sessions/talks.html')
        .controller('SessionsCtrl')
        .resolve({
          account: getAccount,
          sessions : getAllSessions,
          sponsors: getAllSponsors,
          favorites : getMyFavorites
        })
        .build())

      .state('sessions', new State(USER_ROLES, 'sessions?search', 'js/sessions/talks.html')
        .controller('SessionsCtrl')
        .resolve({
          account: getAccount,
          sessions : getAllSessions,
          sponsors: getAllSponsors,
          favorites : getMyFavorites
        })
        .build())

      .state('session', new State(USER_ROLES, 'session/:id/:title', 'js/session/session.html').controller('SessionCtrl')
        .resolve({
          account: getAccount,
          session: getSession,
          favorites : getMyFavorites
        })
        .build())

      .state('sessionwt', new State(USER_ROLES, 'session/:id', 'js/session/session.html').controller('SessionCtrl')
        .resolve({
          account: getAccount,
          session: getSession,
          favorites : getMyFavorites
        })
        .build())

      //Participants
      .state('speakers', new State(USER_ROLES, 'speakers?search', 'js/members/speakers.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers,
          type: getTypeMemberSpeaker
        })
        .build())

      .state('sponsors', new State(USER_ROLES, 'sponsors', 'js/members/sponsors.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers,
          type:getTypeMemberSponsor
        })
        .build())

      .state('about', new State(USER_ROLES, 'about', 'js/members/staff.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers,
          type: getTypeMemberStaff
        })
        .build())

      .state('staff', new State(USER_ROLES, 'staff', 'js/members/staff.html').controller('MembersCtrl')
        .resolve({
          members: getAllMembers,
          type: getTypeMemberStaff
        })
        .build())

      .state('member', new State(USER_ROLES, 'member/:type/:id?redirect', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          member: getMember
        })
        .build())

      .state('sponsor', new State(USER_ROLES, 'member/sponsor/:id', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          type: getTypeMemberSponsor,
          member: getMember
        })
        .build())

      .state('profile', new State(USER_ROLES, 'profile/:login', 'js/member/member.html').controller('MemberCtrl')
        .resolve({
          member: getMemberByLogin
        })
        .build())

      .state('interest', new State(USER_ROLES, 'interest/:name', 'js/interest/interest.html')
        .controller('InterestCtrl')
        .resolve({
          sessions : getSessionsByInterest,
          members : getMembersByInterest
        })
        .build())

      .state('multimedia', new State(USER_ROLES, 'multimedia', 'js/multimedia/multimedia.html').
        build())

      .state('conduite', new State(USER_ROLES, 'conduite', 'js/conduite/conduite.html')
        .build())

      .state('faq', new State(USER_ROLES, 'faq', 'js/faq/faq.html')
        .controller('FaqCtrl')
        .build())

      .state('venir', new State(USER_ROLES, 'venir', 'js/venir/venir.html')
        .build())

      .state('cfp', new State(USER_ROLES, 'cfp', 'js/cfp/home/cfp.html')
        .controller('CfpCtrl')
        .resolve({account: getAccount})
        .build())

      .state('cfptalk', new State(USER_ROLES, 'cfptalk/:id', 'js/cfp/talk/cfptalk.html')
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
      .state('logout', new State(USER_ROLES, 'logout', 'js/home/home.html')
        .build())

      .state('authent', new State(USER_ROLES, 'authent?redirect', 'js/login/login.html')
        .controller('LoginCtrl')
        .build())

      .state('passwordlost', new State(USER_ROLES, 'passwordlost', 'js/password-lost/password-lost.html')
        .controller('PasswordLostCtrl')
        .build())

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
        .build())

      .state('rankingmonit', new State(USER_ROLES, 'rankingmonit?type&keynote&workshop&talk', 'js/ranking/ranking-monitoring.html')
        .controller('RankingCtrl')
        .resolve({
          sessions: getSessionRanking,
          account: getAccount
        })
        .build())

      .state('ranking', new State(USER_ROLES, 'ranking?type&keynote&workshop&talk', 'js/ranking/ranking.html')
        .controller('RankingCtrl')
        .resolve({
          sessions: getSessionRanking,
          account: getAccount
        })
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
