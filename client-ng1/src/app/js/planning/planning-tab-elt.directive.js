(function () {

  'use strict';
  angular.module('cesar-home').directive('cesarPlanningTab', function ($window) {
    'ngInject';

    return {
      templateUrl: 'js/planning/planning-tab-elt.directive.html',
      scope: {},
      bindToController: {
        userConnected: '=',
        timeslots: '=',
        rooms: '=',
        display: '=',
        slots: '=',
        day: '@'
      },
      controllerAs: 'ctrl',
      controller : function($scope){
        var ctrl = this;

        function _computeOffset(){
          ctrl.offset =  document.getElementById('planning1').offsetWidth -
            document.getElementById('planninghour1').offsetWidth - 5 +
            'px';
        }

        function _computeHeight(elts){
          elts.forEach(function(elt){
            elt.height = (elt.duration)*2.0 + 'px';
            elt.transversalHeight = ((elt.duration)*2.0 + 2) + 'px';
          });
        }

        //We compute the height of each slot
        for(var slot in ctrl.slots){
          _computeHeight(ctrl.slots[slot]);
        }
        _computeHeight(ctrl.timeslots);


        angular.element($window).bind('resize', function(){
          _computeOffset();
          $scope.$apply();
        });

        ctrl.isTransversalSlot = function(slot, room){
          if(!ctrl.offset){
            _computeOffset();
          }
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
            return ctrl.offset ? ctrl.offset : _computeOffset();
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