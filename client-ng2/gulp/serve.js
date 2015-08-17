var browserSync = require('browser-sync');
var connectModeRewrite = require('connect-modrewrite');
var serveStatic = require('serve-static');

module.exports = function (gulp, config) {

  require('./build-dev.js')(gulp, config);

  var paths = config.paths;


  function browserSyncInit(baseDir, startPath) {
    browserSync.init({
      startPath: startPath || '/index.html',
      server: {
        baseDir: baseDir,
        middleware: [
          connectModeRewrite([
            //Rewrite for the backend calls
            '^/api/(.*)$ http://localhost:8080/api/$1 [P]',
            //Rewrite for HML
            '!\\.\\w+$ /index.html [L]'
          ]),
          serveStatic(paths.build.dist)
        ]
      }
    });
  }

  function watchPath(path, task){
    gulp.watch(path, function(event) {
      gulp.start(task);
      browserSync.reload(event.path);
    });
  }

  gulp.task('serve:dev', ['build:dev', 'watch:dev'], function () {
    browserSyncInit(paths.build.dev, '/index-dev.html');
  });

  gulp.task('serve:dist', ['build:dist'], function () {
    browserSyncInit(paths.build.dist, '/index.html');
  });


  gulp.task('watch:dev', function() {
    watchPath(paths.ts, 'build:dev:js');
    watchPath(paths.html, 'build:dev:html');
    watchPath(paths.less.path, 'build:dev:css');
  });
};
