var CfpPage = require('./cfp.po.js');

describe('Page : cfp', function () {

  var cfpPage = new CfpPage();

  beforeEach(function () {
    cfpPage.get();
  });

  it('should open the CFP page', function () {
    expect(cfpPage.getTitle()).toBe('Call for paper 2016');
  });



});