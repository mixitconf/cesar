//Minifies css file
var csso = require('gulp-csso');
//Replaces files import in HTML
var htmlreplace = require('gulp-html-replace');
//Rename a file
var rename = require('gulp-rename');
//Replaces element in file
var replace = require('gulp-replace');
//Minifies JS
var uglify = require('gulp-uglify');

module.exports = function(gulp, config) {
  var paths = config.paths;
  var timestamp = config.timestamp;

  gulp.task('build:dist', [
    'build:dev',
    'build:dist:font',
    'build:dist:images',
    'build:dist:favicon',
    'build:dist:css:vendors',
    'build:dist:css',
    'build:dist:vendors',
    'build:dist:js',
    'build:dist:index'
  ]);


  gulp.task('build:dist:font', ['build:dev:font'], function () {
    return gulp.src(paths.build.dev + '/fonts')
      .pipe(gulp.dest(paths.build.dist + '/fonts'));
  });
  gulp.task('build:dist:images', ['build:dev:images'], function () {
    return gulp.src(paths.build.dev + '/fonts')
      .pipe(gulp.dest(paths.build.dist + '/img'));
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
  gulp.task('build:dist:vendors', ['build:dev:ng2', 'build:dev:vendors'], function () {
    return gulp.src(paths.build.dev + '/lib/**/*.js')
       .pipe(gulp.dest(paths.build.dist + '/lib'));
  });
  gulp.task('build:dist:js', ['build:dev:js'], function(){
    return gulp.src(paths.build.dev + '/js/**/*.js')
      .pipe(uglify({output: { 'ascii_only': true }}))   // preserve ascii unicode characters such as \u226E
      .pipe(gulp.dest(paths.build.dist + '/js'));
  });
  gulp.task('build:dist:index', function () {
    return gulp.src(paths.index)
      .pipe(htmlreplace({
        'css': 'css/cesar-' + timestamp + '.min.css'
      }))
      .pipe(rename('index.html'))
      .pipe(gulp.dest(paths.build.dist));
  });

};
