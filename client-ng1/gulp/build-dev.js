//Concats files
var concat = require('gulp-concat');
//Convert HTML teplates in JS
var html2js = require('gulp-ng-html2js');
//Compiles less file in css file
var less = require('gulp-less');
//Merges several files
var merge = require('merge-stream');
//Changes angular files to prepare minification
var ngAnnotate = require('gulp-ng-annotate');
//Replaces element in file
var replace = require('gulp-replace');
//Writes inline source maps
var sourcemaps = require('gulp-sourcemaps');
//several class utils for gulp
var utils = require('gulp-util');


module.exports = function(gulp, config) {
  var paths = config.paths;
  var timestamp = config.timestamp;

  gulp.task('build:dev', ['build:dev:vendors', 'build:dev:js', 'build:dev:css', 'build:dev:font', 'build:dev:images', 'build:dev:favicon', 'build:dev:html']);


  gulp.task('build:dev:font', function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dev + '/fonts'));
  });
  gulp.task('build:dev:images', function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dev + '/img'));
  });
  gulp.task('build:dev:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dev));
  });
  gulp.task('build:dev:css:vendors', function () {
    return gulp.src(paths.css)
      .pipe(concat('vendors.css'))
      .pipe(gulp.dest(paths.build.dev+ '/css'));
  });
  gulp.task('build:dev:css', ['build:dev:css:vendors'], function () {
    return gulp.src(paths.less.main)
      .pipe(less())
      .pipe(replace('assets/img', '../img'))
      .pipe(replace('../../node_modules/material-design-icons/iconfont', '../fonts'))
      .pipe(gulp.dest(paths.build.dev + '/css'));
  });

  /**
   * build:dev JS : concats generated templates and javascript files
   */
  gulp.task('build:dev:js', function() {

    var tpl = gulp.src(paths.templates)
      .pipe(html2js({
        moduleName: 'cesar.templates',
        prefix: 'js/'
      }));

    var app = gulp.src(paths.js.app)
      .pipe(ngAnnotate({
        'single_quotes': true,
        add: true
      }));

    return merge(app, tpl)
      .pipe(sourcemaps.init())
      .pipe(concat('app.js'))
      .pipe(sourcemaps.write('.'))
      .pipe(gulp.dest(paths.build.dev + '/js'));
  });

  /**
   * Concatenate js vendor libs
   */
  gulp.task('build:dev:vendors', function () {
    return gulp.src(paths.js.vendor)
      .pipe(concat('vendors.js'))
      .pipe(gulp.dest(paths.build.dev + '/js'));
  });

  gulp.task('build:dev:html', function () {
    return gulp.src(paths.html)
      .pipe(gulp.dest(paths.build.dev));
  });

  gulp.task('watch', function() {
    gulp.watch(paths.js.app, ['build:dev:js']);
    gulp.watch([paths.templates], ['build:dev:js']);
    gulp.watch([paths.html], ['build:dev:html']);
    gulp.watch(paths.less.path, ['build:dev:css']);
  });
};
