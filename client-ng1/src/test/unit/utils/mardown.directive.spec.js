"use strict";

describe('cesarMarkdown', function () {

  var $compile, $scope, element;

  beforeEach(module('cesar-utils'));

  beforeEach(inject(function (_$compile_, _$rootScope_) {
    $scope = _$rootScope_;
    $compile = _$compile_;
  }));

  it('should add data in markup <p>', function () {
    $scope.content = 'test';
    element = $compile('<cesar-markdown content="content"></cesar-markdown>')($scope);
    $scope.$digest();
    expect(element.html().trim()).toBe('<p>test</p>');
  });

  it('should interpolate title ', function () {
    $scope.content = 'test\n=====';
    element = $compile('<cesar-markdown content="content"></cesar-markdown>')($scope);
    $scope.$digest();
    expect(element.html().trim()).toBe('<h1>test</h1>');
  });

});