
angular.module('cesar-menu').controller('cesarMenuCtrl', function($scope, cesarSecurity){

  $scope.menus = [
    { id : 'actu', name : 'Actualités', link : 'actualites'},
    { id : 'prog', name : 'Programme', link : '', submenus : [
      { id : 'sub1.1', name : 'Planning', link : 'planning', mobile :true},
      { id : 'sub1.2', name : 'Conférences, ateliers', link : 'talks', mobile :true},
      { id : 'sub1.3', name : 'Ligthning talks', link : 'lightningtalks', mobile :true},
      { id : 'sub1.4', name : 'Mix-Teen', link : 'mix-teen'}
    ]},
    { id : 'partic', name : 'Participants', link : '', submenus : [
      { id : 'sub2.1', name : 'Les speakers', link : 'speakers'},
      { id : 'sub2.2', name : 'Les sponsors', link : 'sponsors'},
      { id : 'sub2.3', name : 'Les membres du staff', link : 'staff'}
    ]},
    { id : 'info', name : 'Informations pratiques', link : '' , submenus : [
      { id : 'sub3.1', name : 'A propos de Mix-IT', link : 'apropos'},
      { id : 'sub3.2', name : 'Venir à la conférence', link : 'acces', mobile :true},
      { id : 'sub3.3', name : 'FAQ', link : 'faq', mobile :true},
      { id : 'sub3.4', name : 'Kit multimédia', link : 'multimedia'},
      { id : 'sub3.5', divider : 'true'},
      { id : 'sub3.6', name : 'Mix-IT 2015', link : 'mixit15'},
      { id : 'sub3.7', name : 'Mix-IT 2014', link : 'mixit14'},
      { id : 'sub3.8', name : 'Mix-IT 2013', link : 'mixit13'},
      { id : 'sub3.9', name : 'Mix-IT 2012', link : 'mixit12'}
    ]}
  ];

  cesarSecurity.getUserConnected().then(function(response) {
    $scope.userConnected = response;
    $scope.menus.push(
      { id : 'secure', name : response.name, link : '' , img : response.img, submenus : [
        { id : 'sub4.1', name : 'Mes favoris', link : 'favoris', mobile :true},
        { id : 'sub4.2', name : 'Mon compte', link : 'compte'},
        { id : 'sub4.3', divider : 'true', mobile :true},
        { id : 'sub4.4', name : 'Se déconnecter', link : 'logout', mobile :true}
      ]}
    );
  });
});

angular.module('cesar-menu').directive('cesarMenu', function(){
  return {
    templateUrl: 'js/menu/menu.directive.html',
    scope : {},
    controller : 'cesarMenuCtrl'
  };
});
