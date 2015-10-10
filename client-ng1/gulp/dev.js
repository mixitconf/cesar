module.exports = function(gulp, config) {

  require('./serve.js')(gulp, config);

  var paths = config.paths;


  gulp.task('watch', function() {
    gulp.watch(paths.js.app, ['build:dev:js']);
    gulp.watch([paths.templates], ['build:dev:js']);
    gulp.watch([paths.html], ['build:dev:index']);
    gulp.watch(paths.less.path, ['build:dev:css']);
  });

  gulp.task('dev', ['clean', 'watch'], function() {
    return gulp.start('serve:dev');
  });

};
