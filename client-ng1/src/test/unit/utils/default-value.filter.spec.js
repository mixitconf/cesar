describe('defaultValue', function(){
  var defaultValue;

  beforeEach(module('cesar-utils'));

  beforeEach(inject(function($filter){
    defaultValue = $filter('defaultValue');
  }));

  it('should return "???" by default', function(){
    expect(defaultValue()).toBe('???');
  });

  it('should return the same value', function(){
    expect(defaultValue('same')).toBe('same');
  });

});