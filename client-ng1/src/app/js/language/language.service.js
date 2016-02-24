(function () {

  'use strict';

  angular.module('cesar-members').factory('LanguageService', function ($rootScope, $translate, LocalStorageService, LANGUAGES) {
    'ngInject';

    function toggleLanguage(){
      var currentLanguage = ($translate.use() === LANGUAGES.en) ? LANGUAGES.fr : LANGUAGES.en;
      LocalStorageService.put('default-language', currentLanguage=== LANGUAGES.en ? 'en' : 'fr');
      $translate.use(currentLanguage);
      $rootScope.$broadcast('event:language-changed', currentLanguage);
      return currentLanguage;
    }

    function setDefaultLanguage(){
      var defaultLanguage;

      //We see if user has preference
      var lang = LocalStorageService.get('default-language');
      if(lang){
        defaultLanguage = (lang === 'en') ? LANGUAGES.en : LANGUAGES.fr;
      }
      else{
        //If user is logged we use the language used with his account
        var currentUser = LocalStorageService.get('current-user');

        if(currentUser && currentUser.defaultLanguage) {
          defaultLanguage = (currentUser.defaultLanguage === 'en') ? LANGUAGES.en : LANGUAGES.fr;
        }
        else {
          defaultLanguage = LANGUAGES.fr;
        }
      }

      $translate.use(defaultLanguage);
      $rootScope.$broadcast('event:language-changed', defaultLanguage);
      return defaultLanguage;
    }

    function getLanguages(){
      return LANGUAGES;
    }

    return {
      setDefaultLanguage: setDefaultLanguage,
      toggleLanguage : toggleLanguage,
      getLanguages : getLanguages
    };
  });
})();