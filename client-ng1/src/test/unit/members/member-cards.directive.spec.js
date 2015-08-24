"use strict";

describe('cesarMemberCards', function () {

  var $compile, $scope, element;

  beforeEach(module('cesar-members'));

  beforeEach(inject(function (_$compile_, _$rootScope_) {
    $scope = _$rootScope_;
    $compile = _$compile_;
  }));

  describe('without data', function() {

    it('should not display something when members are empty', function () {
      element = $compile('<cesar-member-cards></cesar-member-cards>')($scope);
      $scope.$digest();
      expect(element.text().trim()).toBe('');
    });

  });

  describe('with speakers', function(){

    beforeEach(function(){
      $scope.members = [
        {firstname : 'James', lastname : 'Gosling'},
        {firstname : 'Jean François', lastname : 'Zobrist'}
      ]
    });

    describe('with length', function() {

      it('should limited the length of a card', function () {
        element = $compile('<cesar-member-cards length="6" members="members"></cesar-member-cards>')($scope);
        $scope.$digest();
        expect(element.find('.cesar-card').length).toBe(2);
        expect(element.find('.cesar-card').hasClass('mdl-cell--6-col')).toBeTruthy();
      });

      it('should limited the length of a card at 4 by line if nothing is defined', function () {
        element = $compile('<cesar-member-cards members="members"></cesar-member-cards>')($scope);
        $scope.$digest();
        expect(element.find('.cesar-card').hasClass('mdl-cell--4-col')).toBeTruthy();
      });

    });

    describe('with search property', function() {
      it('should filter the list', function () {
        $scope.search = undefined;
        element = $compile('<cesar-member-cards members="members" search="search"></cesar-member-cards>')($scope);
        $scope.$digest();

        expect(element.find('.cesar-card').length).toBe(2);

        $scope.search = 'james';
        $scope.$digest();
        expect(element.find('.cesar-card').length).toBe(1);
      });
    });


  });

});