(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-members').controller('MembersCtrl', function (members) {
    var ctrl = this;
    ctrl.members = members;
  });
})();