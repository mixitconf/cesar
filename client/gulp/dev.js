module.exports = function(gulp, config) {

  require('./serve.js')(gulp, config);

  var paths = config.paths;

  /**
   * Index file
   */
  gulp.task('index:dev', function () {
    return gulp.src('app/index.html')
      .pipe(gulp.dest(config.paths.dev));
  });

  gulp.task('watch', function() {
    gulp.watch(paths.js.app, ['bundle:js']);
    gulp.watch([paths.templates], ['bundle:js']);
    gulp.watch([paths.index], ['bundle:index']);
    gulp.watch(paths.less, ['bundle:css']);
  });

  gulp.task('dev', ['clean', 'watch'], function() {
    return gulp.start('serve:dev');
  });

};
