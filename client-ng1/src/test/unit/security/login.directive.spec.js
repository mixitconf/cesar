"use strict";

describe('Directive login', function () {

  var $compile, $scope, $httpBackend, element, frm;

  beforeEach(module('cesar-security'));

  beforeEach(inject(function ($injector) {
    $scope = $injector.get('$rootScope').$new();
    $compile = $injector.get('$compile');
    $httpBackend = $injector.get('$httpBackend');

    $scope.credentials  = {};

    element = $compile('<form name="frm"><input type="text" ng-required="true" name="login" cesar-login ng-model="credentials.login"/></form>')($scope);

    $scope.$digest();
    frm = $scope.frm;
  }));

  it('should be invalid when empty because value is required', function () {
    expect(frm.login.$error.required).toBeTruthy();
  });

  it('should be invalid when login already exists', function () {
    $httpBackend.expectGET('/app/account/check/test').respond({ exists : 'true'});

    $scope.credentials.login = "test";
    $scope.$digest();
    $httpBackend.flush();
    expect(frm.login.$error.uniqueLogin).toBeTruthy();
  });

  it('should be valid when login not exists', function () {
    $httpBackend.expectGET('/app/account/check/test').respond(500, '');

    $scope.credentials.login = "test";
    $scope.$digest();
    $httpBackend.flush();
    expect(frm.login.$valid).toBeTruthy();
  });

});