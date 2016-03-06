(function () {

  'use strict';
  /*global moment */

  angular.module('cesar-home').directive('cesarHomeCountdown', function ($timeout, $interval, $rootScope) {
    'ngInject';

    return {
      templateUrl: 'js/home/countdown.directive.html',
      scope: {},
      controller : function($scope){
        var timer;
        var start;

        function _countdown(){
          var duration = moment.duration(moment().diff(start));

          $scope.countdown = {
            days : Math.trunc(duration.asDays())*-1,
            hours : Math.trunc(duration.asHours())*-1,
            minutes : Math.trunc(duration.asMinutes())*-1,
            end : duration.asMinutes()>0 && duration.asMinutes()> (35*60)
          };
        }

        $timeout(function(){
          start = moment($rootScope.cesar.day1).hours('8');
          timer = $interval(_countdown, 60000);
          _countdown();
        });

        $scope.$on('$destroy', function() {
          if (angular.isDefined(timer)) {
            $interval.cancel(timer);
            timer = undefined;
          }
        });
      }
    };
  });
})();