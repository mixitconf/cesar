angular.module('cesar-members').controller('MemberCtrl', function($http, $state){
  "use strict";

  var ctrl = this;

  $http.get('/api/member/' + $state.current.data.member).then(function(response){
    ctrl.members = response.data;
  });

});