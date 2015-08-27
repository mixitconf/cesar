describe('sessionLogo', function(){
  var sessionLogo;

  beforeEach(module('cesar-sessions'));

  beforeEach(inject(function($filter){
    sessionLogo = $filter('sessionLogo');
  }));

  it('should return "forward_5" by default', function(){
    expect(sessionLogo()).toBe('forward_5');
  });

  it('should return "local_library" for a Talk', function(){
    expect(sessionLogo('Talk')).toBe('local_library');
  });

  it('should return "face" for a Keynote', function(){
    expect(sessionLogo('Keynote')).toBe('face');
  });

  it('should return "build" for a Workshop', function(){
    expect(sessionLogo('Workshop')).toBe('build');
  });
});