(function () {

  'use strict';
  /*global SockJS */
  /*global Stomp */

  angular.module('cesar-planning').controller('RankingCtrl', function ($filter, $stateParams, $state, $scope,  sessions, account) {
    'ngInject';

    var ctrl = this;
    ctrl.nbPositiveVotes = {};

    ctrl.display = {
      keynote : $stateParams.keynote ? $stateParams.keynote === 'true' : false,
      workshop : $stateParams.workshop ? $stateParams.workshop === 'true' : true,
      talk : $stateParams.talk ? $stateParams.talk === 'true' : true,
      type : $stateParams.type ? $stateParams.type : 'bypercent'
    };
    ctrl.userConnected = !!account;

    ctrl.changeType = function(init){
      if(!init){
        $stateParams.type = ctrl.display.type;
        $state.go($state.current.name, $stateParams, {reloadOnSearch: false, notify:false});
      }
      if(ctrl.display.type === 'bypercent'){
        _applyFunction(_orderByRatio);
      }
      else{
        _applyFunction(_orderByPositiveVotes);
      }
    };

    ctrl.changeFormat = function(){
      $stateParams.keynote = ctrl.display.keynote;
      $stateParams.workshop = ctrl.display.workshop;
      $stateParams.talk = ctrl.display.talk;
      $state.go($state.current.name, $stateParams, {reloadOnSearch: false, notify:false});
    };

    //We need to calculate the ratio for positive votes
    function _computeRatio(type){
      if(sessions[type]){
        var cpt=0;
        ctrl.nbPositiveVotes[type] = 0;
        sessions[type].forEach(function(elt){
          elt.ratio = Math.round(elt.positiveVotes / elt.votes * 100);
          if(cpt<5){
            ctrl.nbPositiveVotes[type] += elt.positiveVotes;
            cpt++;
          }
        });
      }
    }
    function _orderByRatio(type){
      if(sessions[type]){
        sessions[type] = $filter('orderBy')(sessions[type], '-ratio');
      }
    }
    function _orderByPositiveVotes(type){
      if(sessions[type]){
        sessions[type] = $filter('orderBy')(sessions[type], '-positiveVotes');
      }
    }
    function _applyFunction(myFunction) {
      myFunction('Keynote');
      myFunction('Workshop');
      myFunction('Talk');
      ctrl.votes = sessions;
    }
    function _refresh(){
      _applyFunction(_orderByPositiveVotes);
      _applyFunction(_computeRatio);
    }

    ctrl.changeType(true);

    var stompClient = null;

    function _connectWs() {
      var socket = new SockJS('/rankings');
      stompClient = Stomp.over(socket);
      stompClient.connect({}, function() {
        stompClient.subscribe('/topic', function(message){
          $scope.$apply(function(){
            sessions = JSON.parse(message.body);
          });
          _refresh();
        });
      });
    }
    _refresh();
    _connectWs();

    // remove route event listener
    $scope.$on('$destroy', function () {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
    });
  });
})();
