var HomePage = require('./home.po.js');

describe('Page : home', function () {

  var homepage = new HomePage();

  beforeEach(function () {
    homepage.get();
  });

  it('should have the title "Mix-IT"', function () {
    expect(homepage.getTitle()).toBe('Mix-IT');
  });

  it('should translate site in english when I click on the button with the american flag', function () {
    expect(homepage.getHeaderMessage()).toBe('revient les 21 et 22 avril 2016');
    homepage.setLanguage('american');
    expect(homepage.getHeaderMessage()).toBe('returns on April 21st and 22nd, 2016');
  });

  it('should translate site in english and return in french', function () {
    expect(homepage.getHeaderMessage()).toBe('returns on April 21st and 22nd, 2016');
    homepage.setLanguage('french');
    expect(homepage.getHeaderMessage()).toBe('revient les 21 et 22 avril 2016');
  });

});