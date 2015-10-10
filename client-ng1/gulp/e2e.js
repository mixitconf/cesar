//Replaces element in file
var protractor = require('gulp-protractor').protractor;
var webdriver_update = require('gulp-protractor').webdriver_update;
var browserSync = require('browser-sync');

module.exports = function(gulp, config) {
  var paths = config.paths;

  require('./build-e2e.js')(gulp, config);
  require('./serve.js');

  gulp.task('e2e:webdriver-update', webdriver_update);

  gulp.task('e2e', ['build:e2e', 'serve:e2e', 'e2e:webdriver-update'], function () {

    gulp.src(paths.js.e2e)
      .pipe(protractor({
        configFile: __dirname + '/../src/test/e2e/protractor.conf.js',
        args: ['--baseUrl', 'http://localhost:12001']
      }))
      .on('error', function (e) {
        throw e;
      })
      .on('end', function () {
        browserSync.exit();
        process.exit();
      });
  });

};