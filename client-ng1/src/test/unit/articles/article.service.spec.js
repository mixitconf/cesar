describe('Service ArticleService', function () {
  var service, $httpBackend;

  beforeEach(module('cesar-articles'));

  beforeEach(inject(function ($injector) {
    $httpBackend = $injector.get('$httpBackend');
    service = $injector.get('ArticleService');
  }));

  describe('getter', function(){
    it('should read all the articles', function () {
      $httpBackend.expectGET('/api/article').respond([
        { title : 'Java and you'}
      ]);
      service.getAll().then(function(response){
        expect(response.data.length).toBe(1);
      });
      $httpBackend.flush();

    });

    it('should read an article', function () {
      $httpBackend.expectGET('/api/article/1').respond(
        { title : 'Java and you'}
      );
      service.getById('1').then(function(response){
        expect(response.data.title).toBe('Java and you');
      });
      $httpBackend.flush();
    });
  });

  describe('navigate', function(){
    var articles;

    beforeEach(function(){
      articles = [
        { id : 154,  name : 'article154' },
        { id : 145,  name : 'article145' },
        { id : 134,  name : 'article134' },
        { id : 98,  name : 'article98' },
        { id : 76,  name : 'article76' },
        { id : 34,  name : 'article76' }
      ];
    });

    it('should return the firsts elements from list', function () {
      expect(service.getClosestIds(articles, 154, 3)).toEqual([154, 145, 134]);
      expect(service.getClosestIds(articles, 154, 1)).toEqual([154]);
      expect(service.getClosestIds(articles, 134, 3)).toEqual([134, 98, 76]);
      expect(service.getClosestIds(articles, 76, 3)).toEqual([76, 34]);
    });

    it('should return the previous elements from list', function () {
      expect(service.getPreviousIds(articles, 154, 3)).toEqual([145, 134, 98]);
      expect(service.getPreviousIds(articles, 76, 3)).toEqual([34]);
      expect(service.getPreviousIds(articles, 156, 3)).toEqual([]);
      expect(service.getPreviousIds(articles, 34, 3)).toEqual([]);
    });

    it('should return the next elements from list', function () {
      expect(service.getNextIds(articles, 98, 3)).toEqual([154, 145, 134]);
      expect(service.getNextIds(articles, 145, 3)).toEqual([154]);
      expect(service.getNextIds(articles, 156, 3)).toEqual([]);
      expect(service.getNextIds(articles, 154, 3)).toEqual([]);
    });
  });




});