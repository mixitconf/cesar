var browserSync = require('browser-sync');
var connectModeRewrite = require('connect-modrewrite');
var serveStatic = require('serve-static');

module.exports = function (gulp, config) {

  require('./build-dev.js')(gulp, config);
  require('./build-dist.js')(gulp, config);

  var paths = config.paths;

  var staticProxyfiedFiles = [
    /^\/api\/.*/,               // http://localhost:8080/api/...
  ];


  function browserSyncInit(baseDir, files, startPath) {
    browserSync.init(files, {
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


  /**
   * This task is not working with the WebSocket connection, but SockJS falls back on long-polling
   * so the live reload in preview still work
   */
  gulp.task('serve:dev', ['build:dev', 'watch'], function () {
    browserSyncInit(paths.build.dev, [
      paths.build.dev + '/**/*.js',
      paths.build.dev + '/**/*.css'
    ], '/index-dev.html');
  });

  gulp.task('serve:dist', ['build:dist', 'watch'], function () {
    browserSyncInit(paths.build.dist, [
      paths.build.dist + '/**/*.js',
      paths.build.dist + '/**/*.css'
    ], '/index.html');
  });

};
