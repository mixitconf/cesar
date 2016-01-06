(function () {

  'use strict';
  /*global componentHandler */

  angular.module('cesar-menu').controller('cesarMenuCtrl', function ($state, $translate, $filter, $timeout, $scope, $rootScope, LANGUAGES, USER_ROLES, AuthenticationService) {
    'ngInject';

    var ctrl = this;

    ctrl.go = function(page){
      $state.go(page);
    };

    ctrl.languages = LANGUAGES;
    $timeout(function(){
      ctrl.currentLanguage = $translate.use() ? $translate.use() : LANGUAGES.fr;
    });
    ctrl.toggleLanguage = function () {
      ctrl.currentLanguage = ($translate.use() === LANGUAGES.en) ? LANGUAGES.fr : LANGUAGES.en;
      $translate.use(ctrl.currentLanguage);
      $rootScope.$broadcast('event:language-changed', ctrl.currentLanguage);
    };

    ctrl.menus = [

      {id: 'menu.news', link: 'news'},
      //{
      //  id: 'menu.program', submenus: [
      //    //{id: 'planning', link: 'planning', mobile: true},
      //    {id: 'menu.talks', link: 'talks', mobile: true},
      //    {id: 'menu.ligthningtalks', link: 'lightningtalks', mobile: true}
      //  ]
      //},
      {
        id: 'menu.members', submenus: [
        //{id: 'menu.speakers', link: 'speakers'},
        {id: 'menu.sponsors', link: 'sponsors', mobile: true},
        {id: 'menu.staff', link: 'staff', mobile: true},
        {id: 'sub2.5', divider: 'true', mobile: true},
      ]
      },
      {
        id: 'menu.info', submenus: [
        {id: 'menu.come', link: 'venir', mobile: true},
        {id: 'menu.faq', link: 'faq', mobile: true},
        {id: 'menu.kit', link: 'multimedia'},
        {id: 'sub3.5', divider: 'true'},
        {id: 'menu.mixit15', link: 'mixit15', mobile: true},
        {id: 'menu.mixit14', link: 'mixit14', mobile: true},
        {id: 'menu.mixit13', link: 'mixit13'},
        {id: 'menu.mixit12', link: 'mixit12'}
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
                {id: 'menu.monitor', link: 'monitor'}
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
