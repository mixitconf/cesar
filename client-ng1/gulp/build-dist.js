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

  gulp.task('build:dist', [
    'build:dev',
    'build:e2e',
    'ddescriber',
    'jshint',
    'build:dist:css:vendors',
    'build:dist:css',
    'build:dist:js',
    'build:dist:vendors',
    'build:dist:font',
    'build:dist:images',
    'build:dist:docs',
    'build:dist:i18n',
    'build:dist:sitemap',
    'build:dist:favicon',
    'build:dist:html',
    'build:dist:index',
    'test']);

  /**
   * Checks for ddescribe and iit
   */
  gulp.task('ddescriber', function () {
    return gulp.src(paths.js.unit)
      .pipe(ddescriber());
  });
  gulp.task('jshint', function(){
    return gulp.src(paths.js.app)
      .pipe(jshint())
      .pipe(jshint.reporter('jshint-stylish'))
      .pipe(jshint.reporter('fail'));
  });

  gulp.task('build:dist:font', ['build:dev:font'], function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dist + '/fonts'));
  });
  gulp.task('build:dist:i18n', ['build:dev:i18n'], function () {
    return gulp.src(paths.assets.i18n)
      .pipe(gulp.dest(paths.build.dist + '/i18n'));
  });
  gulp.task('build:dist:sitemap', ['build:dev:sitemap'], function () {
    return gulp.src(paths.assets.sitemap)
        .pipe(gulp.dest(paths.build.dist + '/'));
  });
  gulp.task('build:dist:images', ['build:dev:favicon', 'build:dev:images'], function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dist + '/img'));
  });
  gulp.task('build:dist:files', ['build:dev:files'], function () {
    return gulp.src(paths.assets.files)
      .pipe(gulp.dest(paths.build.dist + '/files'));
  });
  gulp.task('build:dist:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dist));
  });
  gulp.task('build:dist:css:vendors', ['build:dev:css:vendors'], function(){
    return gulp.src(paths.build.dev + '/css/vendors.css')
      .pipe(rename('vendors-' + timestamp + '.min.css'))
      .pipe(gulp.dest(paths.build.dist + '/css'));
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
  gulp.task('build:dist:html', ['build:dev:html'], function () {
    return gulp.src(paths.build.dev + '/views/**/*.html')
      .pipe(gulp.dest(paths.build.dist + '/views'));
  });
  gulp.task('build:dist:index', function () {
    return gulp.src(paths.index)
      .pipe(htmlreplace({
        'js': 'js/cesar-' + timestamp + '.min.js',
        'vendors': 'js/vendors-' + timestamp + '.min.js',
        'css': 'css/cesar-' + timestamp + '.min.css',
        'vendorscss': 'css/vendors-' + timestamp + '.min.css',
      }))
      .pipe(rename('index.html'))
      .pipe(gulp.dest(paths.build.dist));
  });
};
