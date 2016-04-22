(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('PlanningCtrl', function ($filter, $state, $stateParams, rooms, sessions, transversalSlots, shuffleService) {
    'ngInject';

    var ctrl = this;

    ctrl.slot = {};
    ctrl.slot.format = $stateParams.format ? $stateParams.format : undefined;
    ctrl.slot.displayMode = $stateParams.mode ? $stateParams.mode : 'timeline';
    ctrl.rooms = rooms;

    if($stateParams.room){
      var result = ctrl.rooms
        .filter(function(elt){
          return elt.name === $stateParams.room;
        });
      ctrl.slot.room = result.length > 0 ? result[0] : undefined;
    }

    if (transversalSlots) {
      transversalSlots.forEach(function (elt) {
        var session = {
          title: elt.label,
          end: elt.end,
          start: elt.start
        };
        if (elt.room) {
          session.room = elt.room;
        }
        sessions.push(session);
      });
    }

    //We keep only sessions with a start time
    ctrl.sessions = sessions.filter(function (elt) {
      return elt.start;
    });


    ctrl.shuffle = shuffleService.createShuffle('start');

    ctrl.updateData = function(){
      var sess = angular.copy(ctrl.sessions);
      var params = {};
      if(ctrl.slot.room && ctrl.slot.room.name){
        params.room = ctrl.slot.room.name;
        sess = $filter('filter')(sess, ctrl.slot.room.name);
      }
      if(angular.isDefined(ctrl.slot.format)){
        params.format = ctrl.slot.format;
        sess = $filter('filter')(sess, ctrl.slot.format);
      }
      params.mode = ctrl.slot.displayMode;
      ctrl.shuffle.set(sess);
      $state.go('planning', params, {reloadOnSearch:false, notify:false});
    };

    ctrl.displayElement = function(session, index){
      if(ctrl.slot.displayMode === 'timeline'){
        return moment(session.end).isAfter(moment()) && ctrl.shuffle.displayItem(index);
      }
      if(ctrl.slot.displayMode === 'en'){
        return session.lang === 'en' && ctrl.shuffle.displayItem(index);
      }
      return ctrl.shuffle.displayItem(index);
    };

    ctrl.updateData();
  });
})();
