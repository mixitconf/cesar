// An example configuration file.
exports.config = {
  directConnect: true,

  // ---- 3. To use remote browsers via Sauce Labs -----------------------------
  // If sauceUser and sauceKey are specified, seleniumServerJar will be ignored.
  // The tests will be run remotely using Sauce Labs.
  sauceUser: 'mixit',
  sauceKey: 'da2039cb-1a59-4ddd-aa59-15b9c0bed0d9',
  // Use sauceSeleniumAddress if you need to customize the URL Protractor
  // uses to connect to sauce labs (for example, if you are tunneling selenium
  // traffic through a sauce connect tunnel). Default is
  // ondemand.saucelabs.com:80/wd/hub
  sauceSeleniumAddress: null,

  // Capabilities to be passed to the webdriver instance.
  capabilities: {
    'browserName': 'chrome'
  },

  // Framework to use. Jasmine 2 is recommended.
  framework: 'jasmine2',

  // A base URL for your application under test. Calls to protractor.get()
  // with relative paths will be prepended with this.
  baseUrl: 'http://localhost:12001',

  // Spec patterns are relative to the current working directly when
  // protractor is called.
  specs: ['./spec/**/*.spec.js'],

  // Options to be passed to Jasmine.
  jasmineNodeOpts: {
    defaultTimeoutInterval: 30000
  },

  onPrepare: function() {
    browser.driver.manage().window().maximize();
  },
};
