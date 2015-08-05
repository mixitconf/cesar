var gulp = require('gulp');

var paths = {
  build: {
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
      'node_modules/jquery/dist/jquery.js',
      'node_modules/materialize-css/bin/materialize.js'
    ]
  },
  templates: [
    'src/app/js/**/*.html'
  ],
  less: {
    main : 'src/app/less/main.less',
    path : [
      'src/app/less/*.less'
    ]
  },
  html : 'src/app/*.html',
  index : 'src/app/index-dev.html',
  assets: {
    fonts: [
      'node_modules/font-awesome/fonts/*.*',
      'node_modules/materialize-css/font/**/*.*',
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

gulp.task('default', function() {
  gulp.start(['build']);
});

module.exports = {
  paths: paths
};