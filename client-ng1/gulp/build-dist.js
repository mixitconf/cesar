//Minifies css file
var csso = require('gulp-csso');
//Throws an exception if tests files contain ddescribe or iit
var ddescriber = require('./ddescriber.js');
//Codition in gulp pipe
var gulpIf = require('gulp-if');
//Replaces files import in HTML
var htmlreplace = require('gulp-html-replace');
//Controls JS files
var jshint = require('gulp-jshint');
//Rename a file
var rename = require('gulp-rename');
//Replaces element in file
var replace = require('gulp-replace');
//Writes inline source maps
var sourcemaps = require('gulp-sourcemaps');
//Minifies JS
var uglify = require('gulp-uglify');
//several class utils for gulp
var utils = require('gulp-util');


module.exports = function(gulp, config) {
  var paths = config.paths;
  var timestamp = config.timestamp;

  require('./build-dev.js')(gulp, config);

  gulp.task('build:dist', ['build:dev', 'ddescriber', 'jshint', 'build:dist:css', 'build:dist:js', 'build:dist:vendors', 'build:dist:font', 'build:dist:images', 'build:dist:favicon', 'build:dist:index', 'test']);

  /**
   * Checks for ddescribe and iit
   */
  gulp.task('ddescriber', function () {
    return gulp.src(paths.tests.unit)
      .pipe(ddescriber());
  });
  gulp.task('jshint', function(){
    return gulp.src(paths.js.app)
      .pipe(jshint())
      .pipe(jshint.reporter('jshint-stylish'))
      .pipe(jshint.reporter('fail'));
  });

  gulp.task('build:dist:font', function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dist + '/fonts'));
  });
  gulp.task('build:dist:images', function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dist + '/img'));
  });
  gulp.task('build:dist:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dist));
  });
  gulp.task('build:dist:css', ['build:dev:css'], function(){
    return gulp.src(paths.build.dev + '/css/main.css')
      .pipe(csso())
      .pipe(rename('cesar-' + timestamp + '.min.css'))
      .pipe(gulp.dest(paths.build.dist + '/css'));
  });
  gulp.task('build:dist:js', ['build:dev:js'], function(){
    return gulp.src(paths.build.dev + '/js/app.js')
      .pipe(rename('cesar-' + timestamp + '.min.js'))
      .pipe(uglify({output: { 'ascii_only': true }}))   // preserve ascii unicode characters such as \u226E
      .pipe(gulp.dest(paths.build.dist + '/js'));
  });

  gulp.task('build:dist:vendors', ['build:dev:vendors'], function(){
    function notMinified(file) {
      return !/(src-min|\.min\.js)/.test(file.path);
    }
    return gulp.src(paths.build.dev + '/js/vendors.js')
      .pipe(sourcemaps.init())
      .pipe(gulpIf(notMinified, uglify()))
      .pipe(rename('vendors-' + timestamp + '.min.js'))
      .pipe(sourcemaps.write('.'))
      .pipe(gulp.dest(paths.build.dist + '/js'));
  });

  gulp.task('build:dist:index', function () {
    return gulp.src(paths.index)
      .pipe(htmlreplace({
        'js': 'js/cesar-' + timestamp + '.min.js',
        'vendors': 'js/vendors-' + timestamp + '.min.js',
        'css': 'css/cesar-' + timestamp + '.min.css'
      }))
      .pipe(rename('index.html'))
      .pipe(gulp.dest(paths.build.dist));
  });
};
