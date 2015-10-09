describe('Service SessionService', function () {
  var service, $httpBackend;

  beforeEach(module('cesar-sessions'));

  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('SessionService');
  }));


  it('should read all the speakers', function () {
    $httpBackend.expectGET('/api/session/talk').respond([
      { title : 'My session'}
    ]);
    service.getAll('talk').then(function(response){
      expect(response.data.length).toBe(1);
    });
    $httpBackend.flush();

  });

  it('should read a session', function () {
    $httpBackend.expectGET('/api/session/1').respond(
      { title : 'My session'}
    );
    service.getById('1').then(function(response){
      expect(response.data.title).toBe('My session');
    });
    $httpBackend.flush();

  });

});