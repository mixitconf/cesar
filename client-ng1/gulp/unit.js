var gulp = require('gulp');
var karma = require('karma').Server;

module.exports = function(gulp, config) {

  require('./build-dev.js')(gulp, config);

  /**
   * unit tests once and exit
   */
  gulp.task('test', ['build:dev:js', 'build:dev:vendors'], function (done) {
    new karma({
      configFile: __dirname + '/../src/test/unit/karma.conf.js',
      singleRun: true
    }, done).start();
  });



  /**
   * unit tests in autowatch mode
   */
  gulp.task('test:watch', ['build:dev:js', 'build:dev:vendors'], function (done) {
    new karma({
      configFile: __dirname + '/../src/test/unit/karma.conf.js',
      singleRun: false
    }, done).start();
  });
};
