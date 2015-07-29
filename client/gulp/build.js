//Concats files
var concat = require('gulp-concat');
//Minifies css file
var csso = require('gulp-csso');
//Throws an exception if tests files contain ddescribe or iit
var ddescriber = require('./ddescriber.js');
//Removes a directory or a file
var del = require('del');
//Codition in gulp pipe
var gulpIf = require('gulp-if');
//Convert HTML teplates in JS
var html2js = require('gulp-ng-html2js');
//Replaces files import in HTML
var htmlreplace = require('gulp-html-replace');
//Controls JS files
var jshint = require('gulp-jshint');
//Compiles less file in css file
var less = require('gulp-less');
//Merges several files
var merge = require('merge-stream');
//Changes angular files to prepare minification
var ngAnnotate = require('gulp-ng-annotate');
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

  gulp.task('build', ['clean', 'ddescriber', 'jshint', 'dist:css', 'dist:js', 'dist:vendors', 'dist:font', 'dist:images', 'dist:favicon', 'dist:index', 'test']);

  gulp.task('bundle', ['bundle:vendors', 'bundle:js', 'bundle:css', 'bundle:font', 'bundle:images', 'bundle:favicon', 'bundle:index']);

  /**
   * Cleans the directories created by tasks in this file
   */
  gulp.task('clean', function(done){
    return del('build', done);
  });

  /**
   * Checks for ddescribe and iit
   */
  gulp.task('ddescriber', function () {
    return gulp.src(paths.tests.unit)
      .pipe(ddescriber());
  });

  /**
   * Assets
   */
  gulp.task('bundle:font', function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dev + '/fonts'));
  });
  gulp.task('dist:font', function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dist + '/fonts'));
  });

  gulp.task('bundle:images', function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dev + '/img'));
  });
  gulp.task('dist:images', function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dist + '/img'));
  });

  gulp.task('bundle:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dev));
  });
  gulp.task('dist:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dist));
  });

  /**
   * Compiles css
   */
  gulp.task('bundle:css', function () {
    return gulp.src(paths.less.main)
      .pipe(less())
      .pipe(replace('node_modules/font-awesome/less/font-awesome.less', 'fonts'))
      .pipe(replace('font/material-design-icons', 'fonts/material-design-icons'))
      .pipe(gulp.dest(paths.build.dev + '/css'));
  });

  gulp.task('dist:css', ['bundle:css'], function(){
    return gulp.src(paths.build.dev + '/css/main.css')
      .pipe(csso())
      .pipe(rename('cesar-' + timestamp + '.min.css'))
      .pipe(gulp.dest(paths.build.dist + '/css'));
  });

  /**
   * Bundle JS : concats generated templates and javascript files
   */
  gulp.task('bundle:js', function() {
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

  gulp.task('jshint', function(){
    return gulp.src(paths.js.app)
      .pipe(jshint())
      .pipe(jshint.reporter('jshint-stylish'))
      .pipe(jshint.reporter('fail'));
  });

  gulp.task('dist:js', ['bundle:js'], function(){
    return gulp.src(paths.build.dev + '/js/app.js')
      .pipe(rename('cesar-' + timestamp + '.min.js'))
      .pipe(uglify({output: { 'ascii_only': true }}))   // preserve ascii unicode characters such as \u226E
      .pipe(gulp.dest(paths.build.dist + '/js'));
  });

  /**
   * Concatenate js vendor libs
   */
  gulp.task('bundle:vendors', function () {
    return gulp.src(paths.js.vendor)
      .pipe(concat('vendors.js'))
      .pipe(gulp.dest(paths.build.dev + '/js'));
  });

  gulp.task('dist:vendors', ['bundle:vendors'], function(){
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

  /**
   * Index
   */
  gulp.task('bundle:index', function () {
    return gulp.src(paths.html)
      .pipe(gulp.dest(paths.build.dev));
  });

  gulp.task('dist:index', function () {
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
