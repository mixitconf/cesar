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

    $translateProvider.preferredLanguage(LANGUAGES.fr);
    $translateProvider.useCookieStorage();
    $translateProvider.useSanitizeValueStrategy('sanitize');
  });

})();
