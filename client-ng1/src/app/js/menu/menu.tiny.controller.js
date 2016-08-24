(function () {

  'use strict';

  angular.module('cesar-menu').controller('cesarMenuTinyCtrl', function ($timeout, LanguageService) {
    'ngInject';

    var ctrl = this;

    ctrl.languages = LanguageService.getLanguages();
    $timeout(function(){
      ctrl.currentLanguage = LanguageService.setDefaultLanguage();
    });
    ctrl.toggleLanguage = function () {
      ctrl.currentLanguage = LanguageService.toggleLanguage();
    };

  });
})();
