//Prefix CSS with Autoprefixer to be compatible with all browsers
var autoprefixer = require('gulp-autoprefixer');
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
//Rename a file
var rename = require('gulp-rename');
//Replaces element in file
var replace = require('gulp-replace');
//Writes inline source maps
var sourcemaps = require('gulp-sourcemaps');
//several class utils for gulp
var utils = require('gulp-util');

var pagespeed = require('psi').output;

module.exports = function(gulp, config) {
  var paths = config.paths;

  gulp.task('build:dev', [
    'build:dev:vendors',
    'build:dev:js',
    'build:dev:css',
    'build:dev:font',
    'build:dev:images',
    'build:dev:files',
    'build:dev:i18n',
    'build:dev:sitemap',
    'build:dev:favicon',
    'build:dev:html'
  ]);


  gulp.task('build:dev:font', function () {
    return gulp.src(paths.assets.fonts)
      .pipe(gulp.dest(paths.build.dev + '/fonts'));
  });
  gulp.task('build:dev:i18n', function () {
    return gulp.src(paths.assets.i18n)
      .pipe(gulp.dest(paths.build.dev + '/i18n'));
  });
  gulp.task('build:dev:sitemap', function () {
    return gulp.src(paths.assets.sitemap)
        .pipe(gulp.dest(paths.build.dev + '/'));
  });
  gulp.task('build:dev:images', function () {
    return gulp.src(paths.assets.images)
      .pipe(gulp.dest(paths.build.dev + '/img'));
  });
  gulp.task('build:dev:files', function () {
    return gulp.src(paths.assets.files)
      .pipe(gulp.dest(paths.build.dev + '/files'));
  });
  gulp.task('build:dev:favicon', function () {
    return gulp.src(paths.assets.favicon)
      .pipe(gulp.dest(paths.build.dev));
  });
  gulp.task('build:dev:css:vendors', function () {
    return gulp.src(paths.css)
      //In Angular Material Lite we don't use the standard primary color
      .pipe(replace('"Roboto","Helvetica","Arial",sans-serif', '"Roboto","Arial"'))
      .pipe(replace('"Helvetica","Arial",sans-serif', '"Roboto","Arial"'))
      .pipe(replace('Helvetica,Arial,sans-serif', '"Roboto","Arial"'))
      .pipe(replace('"Roboto","Arial",sans-serif', '"Roboto","Arial"'))
      .pipe(replace('63,81,181', '46,46,46'))
      .pipe(replace('213,0,0', '255,64,129'))
      .pipe(replace('#d50000', 'rgb(255,64,129)'))
      .pipe(replace('#de3226', 'rgb(255,64,129)'))
      .pipe(replace('#3f51b5', '#5A727C'))
      .pipe(concat('vendors.css'))
      .pipe(gulp.dest(paths.build.dev+ '/css'));
  });
  gulp.task('build:dev:css', ['build:dev:css:vendors'], function () {
    return gulp.src(paths.less.main)
      .pipe(less())
      .pipe(replace('assets/img', '../img'))
      .pipe(replace('../../node_modules/material-design-icons/iconfont', '../fonts'))
      .pipe(autoprefixer({
        browsers: ['> 5%'],
        cascade: false
      }))
      .pipe(gulp.dest(paths.build.dev + '/css'));
  });

  /**
   * build:dev JS : concats generated templates and javascript files
   */
  gulp.task('build:dev:js', function() {

    var tpl = gulp.src(paths.templates)
      .pipe(html2js({
        moduleName: 'cesar-templates',
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
    gulp.src(paths.js.external)
      .pipe(gulp.dest(paths.build.dev + '/js'))
    
    return gulp.src(paths.js.vendor)
      .pipe(concat('vendors.js'))
      .pipe(gulp.dest(paths.build.dev + '/js'));
  });

  gulp.task('build:dev:html', function () {
    gulp.src(paths.index)
      .pipe(rename('index.html'))
      .pipe(gulp.dest(paths.build.dev));
    return gulp.src(paths.html)
      .pipe(gulp.dest(paths.build.dev));
  });

  gulp.task('watch', function() {
    gulp.watch(paths.js.app, ['build:dev:js']);
    gulp.watch([paths.templates], ['build:dev:js']);
    gulp.watch([paths.html], ['build:dev:html']);
    gulp.watch(paths.less.path, ['build:dev:css']);
    gulp.watch(paths.assets.i18n, ['build:dev:i18n']);
  });

  // Run PageSpeed Insights
  gulp.task('pagespeed',function(cb){
    // Update the below URL to the public URL of your site
    pagespeed('www.mix-it.fr', {
      strategy: 'mobile'
      // By default we use the PageSpeed Insights free (no API key) tier.
      // Use a Google Developer API key if you have one: http://goo.gl/RkN0vE
      // key: 'YOUR_API_KEY'
    }, cb);
  });
};
