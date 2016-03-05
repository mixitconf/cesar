describe('Filter day', function(){
  var dayFilter;

  beforeEach(module('cesar-utils', 'pascalprecht.translate'));

  beforeEach(inject(function($filter){
    dayFilter = $filter('day');
  }));

  it('should return ""  when no arg', function(){
    expect(dayFilter()).toBe('');
  });

  it('should return formatted text', function(){
    expect(dayFilter('2016-02-03 08:00')).toBe('datetime.day3 3');
  });

});