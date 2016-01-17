describe('Page : home', function () {

  beforeEach(function () {
    browser.get('home');
  });

  it('should have the title "Mix-IT"', function () {
    expect(browser.getTitle()).toBe('Mix-IT');
  });

  it('should translate site in english when I click on the button with the american flag', function () {
    expect(element(by.id('headbandComeback')).getText()).toBe('revient les 21 et 22 avril 2016');

    element(by.className('american-flag')).click();

    expect(element(by.id('headbandComeback')).getText()).toBe('returns on April 21st and 22nd, 2016');
  });
});