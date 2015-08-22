describe('MemberCtrl', function () {
  var ctrl, $httpBackend;

  beforeEach(module('cesar-members'));

  beforeEach(inject(function ($controller, $injector) {

    $httpBackend = $injector.get('$httpBackend');

    ctrl = $controller('MemberCtrl', {
      $http : $injector.get('$http')
    });

  }));


  it('should read all the users', function () {
    $httpBackend.expectGET('/api/member').respond([
      { firstname : 'James', lastname : 'Gosling'}
    ]);
    $httpBackend.flush();
    expect(ctrl.speakers.length).toBe(1);
  });
});