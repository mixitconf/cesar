var gulp = require('gulp');

var paths = {
  build: {
    dev: 'build/dev',
    test: 'build/test',
    dist: 'build/dist'
  },
  js: {
    app : [
      'src/app/js/**/*.js'
    ],
    vendor : [
        'node_modules/angular/angular.js'
      ]
  },
  templates: [
    'src/app/js/**/*.html'
  ],
  less: [
    'src/app/less/main.less'
  ],
  index : 'src/app/index-dev.html',
  assets: {
    fonts: [
      'node_modules/font-awesome/fonts/*.*'
    ],
    images: [
      'src/app/assets/img/*.*',
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

require('./gulp/build.js')(gulp, config);
require('./gulp/unit.js')(gulp, config);
require('./gulp/e2e.js')(gulp, config);
require('./gulp/dev.js')(gulp, config);
require('./gulp/serve.js')(gulp, config);

gulp.task('serve', function() {
  gulp.start('dev');
});

gulp.task('default', ['clean', 'ddescriber'], function() {
  gulp.start(['test', 'build']);
});

module.exports = {
  paths: paths
};