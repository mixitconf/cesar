(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-planning').controller('InterestCtrl', function ($stateParams, $q, $http, $filter, sessions, members) {
    'ngInject';
    var ctrl = this;

    ctrl.name = $stateParams.name;

    ctrl.pagination = {
      member : {
        current: 1,
        nbitems: 5
      },
      session : {
        current: 1,
        nbitems: 5
      }
    };

    ctrl.displayItem = function (index, type){
      var min = ctrl.pagination[type].current * ctrl.pagination[type].nbitems - ctrl.pagination[type].nbitems;
      var max = ctrl.pagination[type].current * ctrl.pagination[type].nbitems - 1;
      return index>=min && index<=max;
    };

    ctrl.filterMember = function(){
      var membersFiltered =  $filter('filter')(members, ctrl.search);
      membersFiltered =  $filter('orderBy')(membersFiltered, ['lastname', 'firstname']);
      ctrl.pagination.member.nbtotal = membersFiltered ? membersFiltered.length : 0;
      ctrl.pagination.member.pages = Math.ceil(ctrl.pagination.member.nbtotal/ctrl.pagination.member.nbitems);
      return membersFiltered;
    };

    ctrl.filterSession = function(){
      var sessionsFiltered =  $filter('filter')(sessions, ctrl.search);
      sessionsFiltered =  $filter('orderBy')(sessionsFiltered, ['-year', 'title']);
      ctrl.pagination.session.nbtotal = sessionsFiltered ? sessionsFiltered.length : 0;
      ctrl.pagination.session.pages = Math.ceil(ctrl.pagination.session.nbtotal/ctrl.pagination.session.nbitems);
      return sessionsFiltered;
    };
  });
})();
