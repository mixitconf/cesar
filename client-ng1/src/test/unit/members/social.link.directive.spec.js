"use strict";

describe('Directive cesarSocialLink', function () {

  var $compile, $scope, element;

  beforeEach(module('cesar-members'));

  beforeEach(inject(function (_$compile_, _$rootScope_) {
    $scope = _$rootScope_;
    $compile = _$compile_;
  }));

  it('should not display href when no url is done', function () {
    element = $compile('<cesar-social-link></cesar-social-link>')($scope);
    $scope.$digest();
    expect(element.find('a').attr('href')).toBeUndefined();
  });

  it('should display href when url is given', function () {
    $scope.link = 'http://mix-it.fr';
    element = $compile('<cesar-social-link url="link"></cesar-social-link>')($scope);
    $scope.$digest();

    expect(element.find('a').attr('href')).toBe('http://mix-it.fr');
  });

  it('should use css class name when it\'s defined', function () {
    element = $compile('<cesar-social-link class-names="maClasseCss"></cesar-social-link>')($scope);
    $scope.$digest();
    expect(element.find('a').hasClass('maClasseCss')).toBeTruthy();
  });

  it('should display a twitter image for twitter link', function () {
    $scope.link = 'https://twitter.com/mixit_lyon';
    element = $compile('<cesar-social-link url="link"></cesar-social-link>')($scope);
    $scope.$digest();
    expect(element.find('img').attr('src')).toBe('img/social/twitter_primary.svg');
  });

  it('should display a google + image for google link', function () {
    $scope.link = 'https://plus.google.com/106629477368826686108/about';
    element = $compile('<cesar-social-link url="link"></cesar-social-link>')($scope);
    $scope.$digest();
    expect(element.find('img').attr('src')).toBe('img/social/google_primary.svg');
  });

  it('should display a default image for others links', function () {
    $scope.link = 'https://toto.com/about';
    element = $compile('<cesar-social-link url="link"></cesar-social-link>')($scope);
    $scope.$digest();
    expect(element.find('img').attr('src')).toBe('http://www.google.com/s2/favicons?domain_url=https://toto.com/about');
  });
});