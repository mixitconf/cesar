(function () {

  'use strict';

  angular.module('cesar-sessions').controller('SessionsCtrl', function (SessionService, MemberService, Util, $state, $rootScope, $timeout, account) {
    'ngInject';

    var ctrl = this;
    var type = $state.current.data.type;

    $timeout(function(){
      var mixitEnd = moment($rootScope.cesar.day2).hours('19');
      //TODO open the vote only between start and stop
      ctrl.voteIsOpen = moment().isBefore(mixitEnd) && $rootScope.cesar.current==='2016';
      ctrl.userConnected = !!account;
    });


    function findSpeaker(response) {
      ctrl.sessions.forEach(function (session) {
        var links = Array.isArray(session.links) ? session.links : [session.links];
        session.speakers = response.data.filter(function (speaker) {
          var found = links.filter(function (s) {
            if(s.rel!=='speaker'){
              return false;
            }
            return Util.extractId(s.href) === (speaker.idMember+'');
          });
          return found.length > 0;
        });
      });
    }

    if (type === 'talks') {
      //we load talks, workshop and keynotes

      SessionService.getAllByYear()
          .then(function (response) {
            ctrl.sessions = response.data;
            return MemberService.getAll('speaker');
          })
          .then(findSpeaker);
    }
    else {
      SessionService.getAll(type)
        .then(function (response) {
          ctrl.sessions = response.data;
          return MemberService.getAllLigthningtalkSpeakers();
        })
        .then(findSpeaker);
    }

  });
})();