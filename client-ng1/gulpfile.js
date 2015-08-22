var gulp = require('gulp');
//Removes a directory or a file
var del = require('del');
//Run in order
var gulpSequence = require('gulp-sequence');

var paths = {
  build: {
    all: 'build',
    dev: 'build/dev',
    test: 'build/test',
    dist: 'build/dist'
  },
  js: {
    app: [
      'src/app/js/**/*.js'
    ],
    vendor: [
      'node_modules/angular/angular.js',
      'node_modules/angular-ui-router/release/angular-ui-router.min.js',
      'node_modules/material-design-lite/material.js'
    ]
  },
  templates: [
    'src/app/js/**/*.html'
  ],
  css : [
    'node_modules/material-design-lite/material.css'
  ],
  less: {
    main : 'src/app/less/main.less',
    path : [
      'src/app/less/*.less'
    ]
  },
  html : 'src/app/**/*.html',
  index : 'src/app/index-dev.html',
  assets: {
    fonts: [
      'node_modules/roboto-fontface/fonts/*.*',
      'node_modules/material-design-icons/iconfont/*.*'
    ],
    images: [
      'src/app/assets/img/**/*.*',
      'src/app/assets/logo/*.*'
    ],
    favicon: [
      'src/app/assets/favicon.ico'
    ]
  },
  tests:{
    unit : [
      'src/test/unit/**/*.spec.js'
    ],
    e2e: [

    ]
  }
};

var config = {
  paths: paths,
  timestamp: Date.now()
};

require('./gulp/build-dev.js')(gulp, config);
require('./gulp/build-dist.js')(gulp, config);
require('./gulp/unit.js')(gulp, config);
require('./gulp/e2e.js')(gulp, config);
require('./gulp/serve.js')(gulp, config);

gulp.task('default', function () {
  gulp.start(['build']);
});

gulp.task('build', function (callback) {
  gulpSequence('clean', 'build:dist', callback);
});

gulp.task('serve', function (callback) {
  gulpSequence('clean', 'serve:dev', callback);
});

gulp.task('clean', function (done) {
  del(paths.build.all, done);
});


module.exports = {
  paths: paths
};