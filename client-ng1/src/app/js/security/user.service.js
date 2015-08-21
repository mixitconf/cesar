
angular.module('cesar-services').factory('cesarSecurity', function($q){

  function getUserConnected(){
    return $q.when({
      id : 1,
      name : 'Guillaume EHRET',
      role : 'ADMIN',
      img : 'avatar.jpg'
    });
  }

  return {
    getUserConnected: getUserConnected
  }
});