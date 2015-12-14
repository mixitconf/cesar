describe('Filter markdown', function(){
  var markdownFilter;

  beforeEach(module('cesar-utils'));

  beforeEach(inject(function($filter){
    markdownFilter = $filter('markdown');
  }));

  it('should return undefined  when no arg', function(){
    expect(markdownFilter()).toBeUndefined();
  });

  it('should return formatted text', function(){
    expect(markdownFilter('Hello.* This is markdown.* It is fun ** Love it.**'))
      .toBe('<p>Hello.<em> This is markdown.</em> It is fun <strong> Love it.</strong></p>');
  });

});