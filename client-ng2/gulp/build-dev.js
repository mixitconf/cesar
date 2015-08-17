//Concats files
var concat = require('gulp-concat');
//Download ng2 js src
var download = require('gulp-download');
//Tests files
var fs = require('fs');
//Compiles less file in css file
var less = require('gulp-less');
//Prevent pipe breaking caused by errors from gulp plugins
var plumber = require('gulp-plumber');
//Rename a file
var rename = require('gulp-rename');
//Replaces element in file
var replace = require('gulp-replace');
//Check TS files
var tslint = require('gulp-tslint');
//typescript
var typescript = require('gulp-typescript');

module.exports = function (gulp, config) {
  var paths = config.paths;

  gulp.task('build:dev', [
    'build:dev:font',
    'build:dev:images',
    'build:dev:favicon',
    'build:dev:css',
    'build:dev:vendors',
    'build:dev:ng2',
    'build:dev:js',
    'build:dev:html'
  ]);


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

  gulp.task('build:dev:ng2', function (done) {
    if (!fs.existsSync(paths.build.dev + '/lib/angular2.min.js')) {
      var ng2Version = require('../package.json').dependencies.angular2;
      return download('https://code.angularjs.org/' + ng2Version + '/angular2.min.js')
        .pipe(gulp.dest(paths.build.dev + '/lib'));
    }
    return done;
  });

  gulp.task('build:dev:vendors', function () {
    return gulp.src(paths.lib)
      .pipe(gulp.dest(paths.build.dev + '/lib'));
  });
  gulp.task('build:dev:js', function () {
    var result = gulp.src(paths.ts)
      .pipe(plumber())
      .pipe(tslint())
      .pipe(tslint.report('verbose'))
      .pipe(typescript({
        noImplicitAny: true,
        module: 'system',
        target: 'ES5',
        experimentalDecorators: true
      }));

    return result.js
      .pipe(gulp.dest(paths.build.dev + '/js'));
  });
  gulp.task('build:dev:html', function () {
    return gulp.src(paths.html)
      .pipe(gulp.dest(paths.build.dev));
  });

};
