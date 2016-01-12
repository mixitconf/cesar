(function () {

  'use strict';

  /**
   * member is resolved in app.js
   */
  angular.module('cesar-cfp').factory('CfpService', function ($http) {
    'ngInject';

    function getParameters(url, i18nkey){
      return $http.get(url).then(function(response){
          var parameters = {};
          response.data.forEach(function(elt){
            parameters[elt] = i18nkey + elt;
          });
        return parameters;
        }
      );
    }

    function getCategories(){
      return getParameters('app/cfp/param/category', 'view.cfp.category.');
    }

    function getFormats(){
      return getParameters('app/cfp/param/format','view.cfp.format.');
    }

    function getSessionTypes(){
      return getParameters('app/cfp/param/typesession', 'view.cfp.typeSession.');
    }

    function getLevels(){
      return getParameters('app/cfp/param/level', 'view.cfp.level.');
    }

    function getMaxAttendees(){
      return getParameters('app/cfp/param/nbattendees', 'view.cfp.nbattendees.');
    }


    return {
      getCategories: getCategories,
      getFormats: getFormats,
      getSessionTypes : getSessionTypes,
      getLevels : getLevels,
      getMaxAttendees: getMaxAttendees
    };

  });
})();
