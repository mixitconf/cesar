(function () {

  'use strict';

  angular.module('cesar-members').factory('MemberService', function ($http) {
    'ngInject';

    function getAll(type, year){
      return $http.get('/api/member/' + type + (year ? '?year=' + year : ''));
    }

    function getAllSponsors(year){
      return $http.get('/api/member/sponsor' + (year ? '?year=' + year : ''));
    }

    function getAllLigthningtalkSpeakers(){
      return $http.get('/api/member/speaker/lightningtalks');
    }

    function getById(id){
      return $http.get('/api/member/' + id);
    }

    function getByLogin(login){
      return $http.get('/api/member/profile/' + login);
    }

    return {
      getAll : getAll,
      getAllSponsors: getAllSponsors,
      getAllLigthningtalkSpeakers: getAllLigthningtalkSpeakers,
      getById : getById,
      getByLogin: getByLogin
    };
  });
})();