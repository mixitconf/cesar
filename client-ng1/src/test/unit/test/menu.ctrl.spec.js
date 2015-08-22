describe('MainCtrl', function () {
  var ctrl, $httpBackend;

  beforeEach(module('cesar'));

  beforeEach(inject(function ($controller, $injector) {

    $httpBackend = $injector.get('$httpBackend');

    ctrl = $controller('MainCtrl', {
      $http : $injector.get('$http')
    });

  }));

  it('should expose a variable in scope', function () {
    expect(ctrl.welcome).toBe('Welcome on mixit');
  });

  it('should read all the users', function () {
    $httpBackend.expectGET('/api/member').respond([
      { firstname : 'James', lastname : 'Gosling'}
    ]);
    $httpBackend.flush();
    expect(ctrl.speakers.length).toBe(1);
  });
});