module.exports = function(gulp, config) {

  require('./serve.js')(gulp, config);

  var paths = config.paths;


  gulp.task('watch', function() {
    gulp.watch(paths.js.app, ['bundle:js']);
    gulp.watch([paths.templates], ['bundle:js']);
    gulp.watch([paths.html], ['bundle:index']);
    gulp.watch(paths.less.path, ['bundle:css']);
  });

  gulp.task('dev', ['clean', 'watch'], function() {
    return gulp.start('serve:dev');
  });

};
