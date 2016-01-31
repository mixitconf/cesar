(function () {

  'use strict';

  angular.module('cesar-planning').factory('PlanningService', function ($http, $q) {
    'ngInject';

    function getRoom(){
      return $http.get('/api/planning/room');
    }

    function getSlots(year){
      return $http.get('/api/planning' + (!!year ? '?year=' + year : ''));
    }

    function computeSlots(year){
      return getSlots(year).then(function(response){
        var slotsByRoom = response.data;

        //Mix-IT journey start at 8:00

        return $q.when(slotsByRoom);
      });
    }


    return {
      getRoom : getRoom,
      getSlots: getSlots,
      computeSlots: computeSlots
    };
  });
})();