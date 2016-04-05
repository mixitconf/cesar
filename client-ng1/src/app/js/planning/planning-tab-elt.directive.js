(function () {

  'use strict';
  angular.module('cesar-home').directive('cesarPlanningTab', function () {
    'ngInject';

    return {
      templateUrl: 'js/planning/planning-tab-elt.directive.html',
      scope: {},
      bindToController: {
        timeslots: '=',
        rooms: '=',
        display: '=',
        slots: '='
      },
      controllerAs: 'ctrl',
      controller : function(){
        var ctrl = this;

        ctrl.computeHeight = function(slot){
          if(slot.duration>60){
            return (slot.duration - 5)*2.0  + 'px';
          }
          return (slot.duration - 2)*2.0  + 'px';
        };

        ctrl.displayRoom = function (room) {
          switch(room.key){
            case 'Amphi1':
              return ctrl.display.amphi;
            case 'Amphi2':
              return ctrl.display.amphi;
            case 'Salle6':
            case 'Salle7':
              return ctrl.display.mezzanine;
          }
          return ctrl.display.salle;
        };
      }
    };
  });
})();