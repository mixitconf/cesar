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

        function _computeSemiOffset(){
          if(!ctrl.display.amphi && !ctrl.display.salle){
            ctrl.semioffset = '0';
          }
          else if(ctrl.display.amphi && !ctrl.display.salle){
            ctrl.semioffset = ctrl.offset;
          }
          else{
            var colWidth = document.getElementById('planning1').offsetWidth - document.getElementById('planninghour1').offsetWidth;
            var nb = 0;
            if(ctrl.display.amphi){
              colWidth = colWidth / 7;
              nb = 4;
            }
            else{
              colWidth = colWidth / 5;
              nb = 2;
            }
            ctrl.semioffset = (colWidth * nb - nb) + 'px';
          }
        }

        function _computeOffset(){
          ctrl.offset =  document.getElementById('planning1').offsetWidth -
            document.getElementById('planninghour1').offsetWidth - 3 +
            'px';
          _computeSemiOffset();
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
            if (ctrl.display.salle) {
              return room.key === 'Salle1';
            }
          }
          return false;
        };

        ctrl.isSemiTransversalSlot = function(slot, room){
          var expectedRoom = ctrl.display.amphi ? 'Amphi1' : 'Salle1';
          if(!!room && room.key===expectedRoom &&  (!!slot.label || (!!slot.session && slot.session.id===3502))){
            return true;
          }
          return false;
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