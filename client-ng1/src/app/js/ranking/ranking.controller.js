(function () {

  'use strict';

  angular.module('cesar-planning').controller('RankingCtrl', function ($filter, sessions, account) {
    'ngInject';

    var ctrl = this;
    ctrl.nbPositiveVotes = {};

    ctrl.display = {
      keynote : true,
      worshop : true,
      talk : true,
      type : 'bypercent'
    };
    ctrl.userConnected = !!account;

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
    _applyFunction(_orderByPositiveVotes);
    _applyFunction(_computeRatio);

    ctrl.changeType = function(){
      if(ctrl.display.type === 'bypercent'){
        _applyFunction(_orderByRatio);
      }
      else{
        _applyFunction(_orderByPositiveVotes);
      }
    };

    ctrl.changeType();

  });
})();
