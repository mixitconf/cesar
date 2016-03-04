(function () {

  'use strict';

  /**
   * This service is used to add pagination in a table
   */
  angular.module('cesar-utils').factory('paginationService', function ($filter) {
    'ngInject';

    function Pagination(propertyOrder){
      this.pagination = {
        current : 1,
        nbitems : 10,
        nbDisplayed : 0
      };
      this.propertyOrder = propertyOrder;
    }

    Pagination.prototype.set = function (data) {
      this.data = data;
    };

    Pagination.prototype.get = function () {
      return this.pagination;
    };

    Pagination.prototype.elements = function (nbelement) {
      return this.pagination.nbDisplayed + ' / ' +  nbelement;
    };

    Pagination.prototype.displayItem = function (index) {
      var min = this.pagination.current * this.pagination.nbitems - this.pagination.nbitems;
      var max = this.pagination.current * this.pagination.nbitems - 1;
      if(index>=min && index<=max){
        this.pagination.nbDisplayed++;
        return true;
      }
      return false;
    };

    Pagination.prototype.filter = function (search) {
      if(this.data){
        var data =  $filter('filter')(this.data, search);
        if(this.propertyOrder){
          data =  $filter('orderBy')(data, this.propertyOrder);
        }
        this.pagination.nbDisplayed = 0;
        this.pagination.nbtotal = data ? data.length : 0;
        this.pagination.pages = Math.ceil(this.pagination.nbtotal/this.pagination.nbitems);
        return data;
      }
      return undefined;
    };

    return {
      createPagination: function(propertyOrder){
        return new Pagination(propertyOrder);
      }
    };
  });

})();
