/**
 * This file uses the Page Object pattern to define the main page for tests
 * https://docs.google.com/presentation/d/1B6manhG0zEXkC-H-tPo2vwU06JhL8w9-XCF9oehXzAQ
 */
'use strict';


var CfpPage = function() {

  var username = element(by.model('ctrl.credentials.username'));
  var password = element(by.model('ctrl.credentials.password'));

  this.get = function() {
    browser.get('/cfp');
    if(username.isEnabled()){
      username.sendKeys('test');
      password.sendKeys('testtest99');
      element.all(by.tagName('button')).last().click();
      browser.sleep(600);
      browser.get('/cfp');
    }
  };

  this.getTitle = function() {
    return element(by.tagName('h1')).getText();
  };


};

module.exports = CfpPage;