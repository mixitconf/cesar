(function () {

  'use strict';
  /*global componentHandler */

  angular.module('cesar-menu').controller('cesarMenuCtrl', function ($state, $filter, $timeout, $scope, $rootScope, AuthenticationService, LanguageService, USER_ROLES) {
    'ngInject';

    var ctrl = this;

    ctrl.go = function(page){
      $state.go(page);
    };

    ctrl.languages = LanguageService.getLanguages();
    $timeout(function(){
      ctrl.currentLanguage = LanguageService.setDefaultLanguage();
    });
    ctrl.toggleLanguage = function () {
      ctrl.currentLanguage = LanguageService.toggleLanguage();
    };

    ctrl.menus = [

      {id: 'menu.news', link: 'article', mobile: true},

      //{id: 'menu.cfp', link: 'cfp', mobile: true},
      //{
      //  id: 'menu.program', submenus: [
      //    //{id: 'planning', link: 'planning', mobile: true},
      //    {id: 'menu.talks', link: 'talks', mobile: true},
      //
      //  ]
      //},
      {
        id: 'menu.conference', submenus: [
        //{id: 'menu.speakers', link: 'speakers'},
        {id: 'menu.sponsors', link: 'sponsors', mobile: true},
        {id: 'menu.staff', link: 'staff', mobile: true},
        {id: 'menu.ligthningtalks', link: 'lightnings', mobile: true},
        {id: 'sub3.5', divider: 'true', mobile: true},
        {id: 'menu.mixit15', link: 'mixit15', mobile: true},
        {id: 'menu.mixit14', link: 'mixit14', mobile: true},
        {id: 'menu.mixit13', link: 'mixit13'},
        {id: 'menu.mixit12', link: 'mixit12'}
      ]
      },
      {
        id: 'menu.info', submenus: [
        {id: 'menu.come', link: 'venir', mobile: true},
        {id: 'menu.faq', link: 'faq', mobile: true},
        {id: 'menu.kit', link: 'multimedia'},
      ]
      }
    ];


    function updateUserSection(){
      ctrl.menus.forEach(function(elt, index){
        if(elt.id === 'menu.admin'){
          ctrl.menus.splice(index, 1);
        }
      });
      AuthenticationService.currentUser().then(function(currentUser){
        if(currentUser){
          ctrl.security = {
            authentified : true,
            hash: currentUser.hash,
            name: currentUser.firstname,
            submenus: [
              //{id: 'menu.favorites', link: 'favoris', mobile: true},
              {id: 'menu.account', link: 'account', mobile : true},
              {id: 'sub4.1', divider: true},
              {id: 'menu.logout', link: 'logout', mobile: true}
            ]
          };
          if(AuthenticationService.isAuthorized(USER_ROLES.admin, currentUser)){
            ctrl.menus.push(
              {
                id: 'menu.admin', name: 'admin', submenus: [
                {id: 'menu.monitor', link: 'monitor'},
                {id: 'menu.cache', link: 'cache'},
                {id: 'menu.news', link: 'admarticles'},
                {id: 'menu.cfp', link: 'admcfp'},
                {id: 'menu.planning', link: 'admplanning'},
                {id: 'menu.accounts', link: 'admaccounts'}
              ]}
            );
          }
          $timeout(function () {
            componentHandler.upgradeAllRegistered();
          });
        }
        else{
          delete ctrl.security;
          ctrl.security = {
            authentified : false
          };
        }
      });
    }

    $scope.$watch('userRefreshed', function(){
      updateUserSection();
    });
  });
})();
