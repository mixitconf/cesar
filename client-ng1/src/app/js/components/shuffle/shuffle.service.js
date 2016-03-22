(function () {

  'use strict';

  /**
   * This service is used to add pagination in a table
   */
  angular.module('cesar-utils').factory('shuffleService', function ($filter) {
    'ngInject';

    function Shuffle(propertyOrder){
      this.shuffle = {
        current : 1,
        nbitems : 10,
        nbDisplayed : 0
      };
      this.propertyOrder = propertyOrder;
    }

    Shuffle.prototype.setNbItems = function (nbitems) {
      this.shuffle.nbitems = nbitems;
    };

    Shuffle.prototype.set = function (data) {
      this.data = data;
    };

    Shuffle.prototype.get = function () {
      return this.shuffle;
    };

    Shuffle.prototype.getNbElts = function () {
      return this.shuffle.nbtotal;
    };

    Shuffle.prototype.elements = function (nbelement) {
      return this.shuffle.nbDisplayed + ' / ' +  nbelement;
    };

    Shuffle.prototype.updateNbDisplayed = function () {
      if((this.shuffle.nbDisplayed + this.shuffle.nbitems) < this.shuffle.nbtotal){
        this.shuffle.nbDisplayed += this.shuffle.nbitems;
      }
      else{
        this.shuffle.nbDisplayed += this.shuffle.nbtotal;
      }
    };

    Shuffle.prototype.more = function () {
      if( this.shuffle.current * this.shuffle.nbitems < this.shuffle.nbtotal){
        this.shuffle.current ++;
      }
    };

    Shuffle.prototype.displayItem = function (index) {
      var max = this.shuffle.current * this.shuffle.nbitems - 1;
      if(index>=0 && index<=max){
        this.shuffle.nbDisplayed++;
        return true;
      }
      return false;
    };

    Shuffle.prototype.displayMoreButton = function () {
      return this.shuffle.current * this.shuffle.nbitems < this.shuffle.nbtotal;
    };

    Shuffle.prototype.filter = function (search) {
      if(this.data){
        var data =  $filter('filter')(this.data, search);

        if(this.propertyOrder){
          data =  $filter('orderBy')(data, this.propertyOrder);
        }
        this.shuffle.nbDisplayed = 0;
        this.shuffle.nbtotal = data ? data.length : 0;

        return data;
      }
      return undefined;
    };

    return {
      createShuffle: function(propertyOrder){
        return new Shuffle(propertyOrder);
      }
    };
  });

})();
