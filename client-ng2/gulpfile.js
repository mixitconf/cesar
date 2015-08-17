//Main module
var gulp = require('gulp');
//Removes a directory or a file
var del = require('del');
//Run in order
var gulpSequence = require('gulp-sequence');

var paths = {
  build: {
    all: 'build',
    dev: 'build/dev',
    dist: 'build/dist'
  },
  ts: 'src/app/ts/**/*.ts',
  lib: [
    'node_modules/angular2/node_modules/traceur/bin/traceur-runtime.js',
    'node_modules/systemjs/dist/system-csp-production.js',
    'node_modules/material-design-lite/material.js'
  ],
  css : [
    'node_modules/material-design-lite/material.css'
  ],
  less: {
    main: 'src/app/less/main.less',
    path: [
      'src/app/less/*.less'
    ]
  },
  html : 'src/app/*.html',
  index: 'src/app/index-dev.html',
  assets: {
    fonts: [
      'node_modules/material-design-icons/iconfont/*.*',
      'node_modules/roboto-fontface/fonts/*.*'
    ],
    images: [
      'src/app/assets/img/**/*.*',
      'src/app/assets/logo/*.*'
    ],
    favicon: [
      'src/app/assets/favicon.ico'
    ]
  },
  tests: {
    unit: [
      'src/test/unit/**/*.spec.js'
    ],
    e2e: []
  }
};

var config = {
  paths: paths,
  timestamp: Date.now()
};

require('./gulp/build-dev.js')(gulp, config);
require('./gulp/build-dist.js')(gulp, config);
require('./gulp/serve.js')(gulp, config);
require('./gulp/unit.js')(gulp, config);
require('./gulp/e2e.js')(gulp, config);


gulp.task('default', function () {
  gulp.start(['build']);
});

gulp.task('build', function (callback) {
  gulpSequence('build:dist', callback);
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