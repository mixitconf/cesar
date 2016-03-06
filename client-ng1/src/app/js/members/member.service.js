(function () {

  'use strict';

  angular.module('cesar-members').factory('MemberService', function ($http) {
    'ngInject';

    function getAll(type, year){
      return $http.get('/api/member/' + type + (year ? '?year=' + year : ''));
    }

    function getAllLigthningtalkSpeakers(){
      return $http.get('/api/member/speaker/lightningtalks?year=2016');
    }

    function getById(id){
      return $http.get('/api/member/' + id);
    }

    function getByLogin(login){
      return $http.get('/api/member/profile/' + login);
    }

    return {
      getAll : getAll,
      getAllLigthningtalkSpeakers: getAllLigthningtalkSpeakers,
      getById : getById,
      getByLogin: getByLogin
    };
  });
})();