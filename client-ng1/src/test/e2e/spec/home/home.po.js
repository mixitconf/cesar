/**
 * This file uses the Page Object pattern to define the main page for tests
 * https://docs.google.com/presentation/d/1B6manhG0zEXkC-H-tPo2vwU06JhL8w9-XCF9oehXzAQ
 */
'use strict';


var HomePage = function() {

  this.get = function() {
    browser.get('/home');
  };

  this.getTitle = function() {
    return browser.getTitle();
  };

  this.setLanguage = function(lang) {
    element(by.className(lang + '-flag')).click();
  };

  this.getHeaderMessage = function() {
    return element(by.id('headbandComeback')).getText();
  };

};

module.exports = HomePage;