// Karma configuration
// Generated on Wed Jul 22 2015 23:11:01 GMT+0200 (CEST)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '../../../',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
      { pattern: 'node_modules/jquery/dist/jquery.js', watched:false },
      { pattern: 'node_modules/angular/angular.js', watched:false },
      { pattern: 'node_modules/angular-sanitize/angular-sanitize.js', watched:false },
      { pattern: 'node_modules/angular-cookies/angular-cookies.js', watched:false },
      { pattern: 'node_modules/angular-ui-router/release/angular-ui-router.min.js', watched:false },
      { pattern: 'node_modules/angular-mocks/angular-mocks.js', watched:false },
      { pattern: 'node_modules/markdown/lib/markdown.js', watched:false },

      'src/app/js/**/app.js',
      'src/app/js/**/*.js',
      'src/app/js/**/*.html',

      'src/test/unit/**/*.spec.js'
    ],


    // list of files to exclude
    exclude: [
    ],

    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'src/app/**/*.js': ['coverage'],
      'src/app/js/**/*.html': ['ng-html2js']
    },

    coverageReporter: {
      type: 'lcov',
      dir: '.tmp/reports/coverage/'
    },

    ngHtml2JsPreprocessor: {
      //The last / is important 
      stripPrefix: 'src/app/',
      moduleName: 'cesar-templates'
    },

    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['PhantomJS'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false
  })
}
