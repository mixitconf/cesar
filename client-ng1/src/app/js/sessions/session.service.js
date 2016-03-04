(function () {

  'use strict';

  angular.module('cesar-sessions').factory('SessionService', function ($http, Util) {
    'ngInject';

    function getAllByYear(year){
      return $http.get('/api/session' + (year ? '?year=' + year : ''));
    }

    function getAll(type){
      return $http.get('/api/session/' + type);
    }

    function getById(id){
      return $http.get('/api/session/' + id);
    }

    function findSessionsSpeakers(sessions, speakers) {
      sessions.forEach(function (session) {
        var links = Array.isArray(session.links) ? session.links : [session.links];

        session.speakers = speakers.filter(function (speaker) {
          var found = links.filter(function (s) {
            if (s.rel !== 'speaker') {
              return false;
            }
            return Util.extractId(s.href) === (speaker.idMember + '');
          });
          return found.length > 0;
        });
      });
    }

    return {
      findSessionsSpeakers: findSessionsSpeakers,
      getAll : getAll,
      getAllByYear : getAllByYear,
      getById : getById
    };
  });
})();