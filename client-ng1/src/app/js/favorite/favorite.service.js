(function () {

  'use strict';

  angular.module('cesar-sessions').factory('FavoriteService', function ($http) {
    'ngInject';

    function markFavorite(session, favorites) {
      favorites.forEach(function (sessionId) {
        if (session.idSession === sessionId) {
          session.favorite = true;
        }
      });
    }

    function _markFavorites(sessions, sessionId) {
      sessions
        .filter(function (session) {
          return session.idSession === sessionId;
        })
        .forEach(function (session) {
          session.favorite = true;
        });
    }

    /**
     * Read all the sessions and add tag favorite if the session is a favorite
     * for the current user
     */
    function markFavorites(sessions, favorites) {
      favorites.forEach(function (sessionId) {
        _markFavorites(sessions, sessionId);
      });
    }

    /**
     * Read all the sessions and add tag favorite if the session is a favorite
     * for the current user
     */
    function markFavoritesInSlots(slots, favorites, rooms) {
      favorites.forEach(function (sessionId) {
        rooms.forEach(function (room) {
          _markFavorites(slots[room.key], sessionId);
        });
      });
    }

    /**
     * Change the status of the session for the current user. If session is marked
     * like favorite we delete this preference. If not we create a new favorite
     */
    function toggleFavorite(session) {
      return $http.put('/app/favorite/' + session.idSession);
    }

    return {
      markFavorite: markFavorite,
      markFavorites: markFavorites,
      markFavoritesInSlots:markFavoritesInSlots,
      toggleFavorite: toggleFavorite
    };
  });
})();