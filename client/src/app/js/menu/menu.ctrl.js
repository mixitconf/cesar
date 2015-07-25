
angular.module('cesar').controller('MenuCtrl', function($http){
  var ctrl = this;

  ctrl.welcome = 'Welcome on mixit';

  $http.get('/api/member').success(function(data){
    ctrl.speakers = data;
  });

});