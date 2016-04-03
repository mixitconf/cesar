(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-planning').controller('PlanningCtrl', function ($filter, $state, $stateParams, rooms, sessions, transversalSlots, shuffleService, favorites, account, FavoriteService) {
    'ngInject';

    var ctrl = this;
    var oldsession;

    FavoriteService.markFavorites(sessions, favorites);

    ctrl.slot = {};
    ctrl.slot.format = $stateParams.format ? $stateParams.format : undefined;
    ctrl.slot.displayMode = $stateParams.mode ? $stateParams.mode : 'timeline';
    ctrl.rooms = rooms;
    ctrl.userConnected = !!account;

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


    ctrl.shuffle = shuffleService.createShuffle(['start', 'room']);
    ctrl.shuffle.setNbItems(70);

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
      var ret = ctrl.shuffle.displayItem(index, true);

      if( session.title==='view.planning.moment.pause'){
        ret &= false;
      }
      oldsession = session.title;

      if(ctrl.slot.displayMode === 'timeline'){
        ret &= moment(session.end).isAfter(moment());
      }
      else if(ctrl.slot.displayMode === 'en'){
        ret &= session.lang === 'en';
      }
      else if(ctrl.slot.displayMode === 'favorite'){
        ret &= session.favorite;
      }

      if(ret){
        ctrl.shuffle.plusDisplayed();
      }
      return ret;
    };

    ctrl.updateData();

    if (ctrl.userConnected) {
      ctrl.toggleFavorite = function(session){
        ctrl.errorMessage = undefined;
        FavoriteService
          .toggleFavorite(session)
          .then(function(){
            session.favorite = !!session.favorite ? false : true;
          })
          .catch(function () {
            ctrl.errorMessage = 'UNDEFINED';
          });
      };
    }
  });
})();
