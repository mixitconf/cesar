describe('Filter markdown', function(){
  var timeFilter;

  beforeEach(module('cesar-utils'));

  beforeEach(inject(function($filter){
    console.log(moment())
    timeFilter = $filter('time');
  }));

  it('should return ""  when no arg', function(){
    expect(timeFilter()).toBe('');
  });

  it('should return formatted text', function(){
    expect(timeFilter('2016-02-03 08:00')).toBe('08:00');
  });

});