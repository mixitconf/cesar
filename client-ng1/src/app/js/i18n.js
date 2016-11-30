(function () {

  'use strict';

  angular.module('cesar').constant('LANGUAGES', {
    fr: 'fr_FR',
    en: 'us_US'
  });

  angular.module('cesar').config(function ($translateProvider, LANGUAGES) {
    'ngInject';

    // Initialize angular-translate
    $translateProvider.useStaticFilesLoader({
      prefix: 'i18n/',
      suffix: '.json'
    });

    // The default language is the browser language
    var language = navigator.languages ? navigator.languages[0] : (navigator.language || navigator.userLanguage);
    if(language.indexOf('fr')>=0){
      $translateProvider.preferredLanguage(LANGUAGES.fr);
    }
    else{
      $translateProvider.preferredLanguage(LANGUAGES.en);
    }

    $translateProvider.useCookieStorage();
    $translateProvider.useSanitizeValueStrategy('sanitize');
  });

})();
