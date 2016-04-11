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
        slots: '=',
        day: '@'
      },
      controllerAs: 'ctrl',
      controller : function(){
        var ctrl = this;

        ctrl.computeHeight = function(slot, transverse){
          return transverse ? (slot.duration)*2.0 + 1  + 'px' : (slot.duration)*2.0  + 'px';
        };

        ctrl.isTransversalSlot = function(slot, room){
          if(!!room && !!slot.room && slot.room.key==='Salle7' &&  (!!slot.label || !!slot.session)){
            if (ctrl.display.amphi) {
              return room.key === 'Amphi1';
            }
            if (ctrl.display.amphi) {
              return room.key === 'Salle1';
            }
            if (ctrl.display.mezzanine) {
              return room.key === 'Salle6';
            }
          }
          return false;
        };

        ctrl.computeWidth = function(slot, room){
          if( ctrl.isTransversalSlot(slot, room)){
            return (document.getElementById('planning1').offsetWidth -
              document.getElementById('planninghour1').offsetWidth - 5) +
              'px';
          }
          return 'auto';
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