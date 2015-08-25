describe('MemberService', function () {
  var service, $httpBackend;

  beforeEach(module('cesar-members'));

  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('MemberService');
  }));


  it('should read all the speakers', function () {
    $httpBackend.expectGET('/api/member/speaker').respond([
      { firstname : 'James', lastname : 'Gosling'}
    ]);
    service.getAll('speaker').then(function(response){
      expect(response.data.length).toBe(1);
    });
    $httpBackend.flush();

  });

  it('should read a member', function () {
    $httpBackend.expectGET('/api/member/1').respond(
      { firstname : 'James', lastname : 'Gosling'}
    );
    service.getById('1').then(function(response){
      expect(response.data.firstname).toBe('James');
    });
    $httpBackend.flush();

  });

});