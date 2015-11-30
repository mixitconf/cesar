(function () {

  'use strict';

  angular.module('cesar-members').factory('MemberService', function ($http) {
    'ngInject';

    function getAll(type, year){
      return $http.get('/api/member/' + type + (year ? '?year=' + year : ''));
    }

    function getAllLigthningtalkSpeakers(){
      return $http.get('/api/member/speaker/lightningtalks');
    }

    function getById(id){
      return $http.get('/api/member/' + id);
    }

    return {
      getAll : getAll,
      getAllLigthningtalkSpeakers: getAllLigthningtalkSpeakers,
      getById : getById
    };
  });
})();