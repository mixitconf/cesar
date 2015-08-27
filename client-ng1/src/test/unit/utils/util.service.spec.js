describe('Util', function () {
  var service;

  beforeEach(module('cesar-utils'));

  beforeEach(inject(function ($injector) {
    service = $injector.get('Util');
  }));


  describe('extractId', function(){
    it('should return undefined when no arg is done', function () {
      expect(service.extractId()).toBeUndefined();
    });

    it('should return 9 when url is "http://localhost:8080/api/member/9"', function () {
      expect(service.extractId('http://localhost:8080/api/member/9')).toBe('9');
    });
  });


});