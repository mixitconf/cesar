(function () {

  'use strict';

  angular.module('cesar-account').controller('MixteenCtrl', function ($http) {
    'ngInject';

    var ctrl = this;

    //    Mireille Delpui : mireille.delpui@gmail.com
    //    Francisco Nogueira : nogueira.frma@yahoo.fr

    $http.post('/api/member/byids', [13,701, 4111, 488, 5582, 14, 48, 33, 7562, 7422, 7722, 7762])
        .then(function (response) {
          ctrl.members = response.data;
        });

  });

})();