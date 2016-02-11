(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-planning').controller('InterestCtrl', function ($stateParams, $q, $http, $scope, $filter) {
    'ngInject';
    var ctrl = this;
    var members, sessions;

    ctrl.name = $stateParams.name;

    $q.all([
      $http.get('/api/member/interest/' + ctrl.name).then(function(response){
        members = response.data;
      }),
      $http.get('/api/session/interest/' + ctrl.name).then(function(response){
        sessions = response.data;
      })
    ]);

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
      var membersFiltered =  $filter('filter')(members, $scope.search);
      membersFiltered =  $filter('orderBy')(membersFiltered, ['lastname', 'firstname']);
      ctrl.pagination.member.nbtotal = membersFiltered ? membersFiltered.length : 0;
      ctrl.pagination.member.pages = Math.ceil(ctrl.pagination.member.nbtotal/ctrl.pagination.member.nbitems);
      return membersFiltered;
    };

    ctrl.filterSession = function(){
      var sessionsFiltered =  $filter('filter')(sessions, $scope.search);
      sessionsFiltered =  $filter('orderBy')(sessionsFiltered, ['-year', 'title']);
      ctrl.pagination.session.nbtotal = sessionsFiltered ? sessionsFiltered.length : 0;
      ctrl.pagination.session.pages = Math.ceil(ctrl.pagination.session.nbtotal/ctrl.pagination.session.nbitems);
      return sessionsFiltered;
    };
  });
})();
