"use strict";

describe('Directive cesarConfirmPassword', function () {

  var $compile, $scope, element, frm, $httpBackend;

  beforeEach(module('cesar-security'));

  beforeEach(inject(function (_$compile_, _$rootScope_) {
    $scope = _$rootScope_;
    $compile = _$compile_;
    $scope.credentials  = {};

    element = $compile('<form name="frm"><input type="password" cesar-confirm-password ng-required="true" name="confirmpassword" ng-model="credentials.confirmpassword" password="credentials.password"/></form>')($scope);

    $scope.$digest();
    frm = $scope.frm;
  }));

  it('should be invalid when empty because value is required', function () {
    expect(frm.confirmpassword.$error.required).toBeTruthy();
  });

  it('should be invalid when confirmpassword is not equal to password', function () {
    $scope.credentials.password = "test";
    $scope.credentials.confirmpassword = "testaaaa";
    $scope.$digest();
    expect(frm.confirmpassword.$error.validPassword).toBeTruthy();
  });

  it('should be valid when confirmpassword is  equal to password', function () {
    $scope.credentials.password = "test";
    $scope.credentials.confirmpassword = "test";
    $scope.$digest();

    expect(frm.confirmpassword.$valid).toBeTruthy();
  });
});