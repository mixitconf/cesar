describe('ArticleService', function () {
  var service, $httpBackend;

  beforeEach(module('cesar-articles'));

  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('ArticleService');
  }));


  it('should read all the articles', function () {
    $httpBackend.expectGET('/crud/article').respond([
      { title : 'Java and you'}
    ]);
    service.getAll().then(function(response){
      expect(response.data.length).toBe(1);
    });
    $httpBackend.flush();

  });

  it('should read an article', function () {
    $httpBackend.expectGET('/crud/article/1').respond(
      { title : 'Java and you'}
    );
    service.getById('1').then(function(response){
      expect(response.data.title).toBe('Java and you');
    });
    $httpBackend.flush();

  });

});