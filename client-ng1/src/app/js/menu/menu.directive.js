(function () {

  'use strict';

  angular.module('cesar-menu').controller('cesarMenuCtrl', function ($translate, $filter, $timeout, LANGUAGES) {
    var ctrl = this;

    ctrl.languages = LANGUAGES;
    $timeout(function(){
      ctrl.currentLanguage = $translate.use() ? $translate.use() : LANGUAGES.fr;
    })
    ctrl.toggleLanguage = function () {
      ctrl.currentLanguage = ($translate.use() === LANGUAGES.us) ? LANGUAGES.fr : LANGUAGES.us;
      $translate.use(ctrl.currentLanguage);
    };


    ctrl.menus = [
      //TODO will be used in the next version
      //{id: 'menu.news', link: 'news'},
      //{
      //  id: 'menu.program', submenus: [
      //  //{id: 'planning', link: 'planning', mobile: true},
      //  {id: 'menu.talks', link: 'talks', mobile: true},
      //  {id: 'menu.ligthningtalks', link: 'lightningtalks', mobile: true}
      //]
      //},
      //{
      //  id: 'menu.members', submenus: [
      //  {id: 'menu.speakers', link: 'speakers'},
      //  {id: 'menu.sponsors', link: 'sponsors'},
      //  {id: 'menu.staff', link: 'staff'}
      //]
      //},
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


    //TODO will be used in the next version
    //$scope.$watch('userConnected', function(newValue){
    //
    //  var lastIndex = $scope.menus.length-1;
    //  if($scope.menus[lastIndex].id === 'secure'){
    //    $scope.menus.splice(lastIndex, 1);
    //  }
    //
    //  if(newValue && newValue.login){
    //    $scope.menus.push(
    //      {
    //        id: 'secure', name: newValue.firstname + ' ' + newValue.lastname, hash: newValue.hash, submenus: [
    //        {id: 'menu.favorites', link: 'favoris', mobile: true},
    //        {id: 'menu.account', link: 'account'},
    //        {id: 'sub4.1', mobile: true},
    //        {id: 'menu.logout', link: 'logout', mobile: true}
    //      ]
    //      }
    //    );
    //  }
    //  else{
    //    $scope.menus.push({id: 'secure', icon: 'vpn_key', link: 'authent', mobile: true});
    //  }
    //});
  });

  angular.module('cesar-menu').directive('cesarMenu', function () {
    return {
      templateUrl: 'js/menu/menu.directive.html',
      controller: 'cesarMenuCtrl',
      controllerAs: 'ctrl'
    };
  });
})();
