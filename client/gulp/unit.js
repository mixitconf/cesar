var gulp = require('gulp');
var karma = require('karma').Server;
var coveralls = require('gulp-coveralls');

module.exports = function(gulp, config) {

  require('./build.js')(gulp, config);

  /**
   * unit tests once and exit
   */
  gulp.task('test', ['bundle:js', 'bundle:vendors'], function (done) {
    new karma({
      configFile: __dirname + '/../karma.conf.js',
      singleRun: true
    }, done).start();
  });

  gulp.task('test:coveralls', ['test'], function (done) {
    gulp.src('.tmp/reports/coverage/**/lcov.info')
      .pipe(coveralls());
  });



  /**
   * unit tests in autowatch mode
   */
  gulp.task('test:watch', ['bundle:js', 'bundle:vendors'], function (done) {
    new karma({
      configFile: __dirname + '/../karma.conf.js',
      singleRun: false
    }, done).start();
  });
};
