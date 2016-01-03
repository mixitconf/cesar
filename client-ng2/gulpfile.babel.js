/**
 *
 *  Web Starter Kit
 *  Copyright 2015 Google Inc. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 *
 */

'use strict';

// This gulpfile makes use of new JavaScript features.
// Babel handles this without us having to do anything. It just works.
// You can read more about the new JavaScript features here:
// https://babeljs.io/docs/learn-es2015/

import fs from 'fs';
import path from 'path';
import gulp from 'gulp';
import del from 'del';
import less from 'gulp-less';
import runSequence from 'run-sequence';
import browserSync from 'browser-sync';
import swPrecache from 'sw-precache';
import gulpLoadPlugins from 'gulp-load-plugins';
import {output as pagespeed} from 'psi';
import pkg from './package.json';
import connectModeRewrite from 'connect-modrewrite';

const $ = gulpLoadPlugins();
const reload = browserSync.reload;

const paths = {
  less: ['src/**/*.less'],
  ts: ['src/**/*.ts'],
  vendor: {
    js: [
      'node_modules/systemjs/dist/system.src.js',
      'node_modules/rxjs/bundles/Rx.js',
      'node_modules/angular2/bundles/angular2.dev.js',
      'node_modules/angular2/bundles/http.dev.js',
      'node_modules/angular2/bundles/router.dev.js',
      'node_modules/material-design-lite/material.js',
      'node_modules/markdown/lib/markdown.js'
    ],
    css:[
      'node_modules/material-design-lite/dist/material.indigo-pink.min.css'
    ],
    polyfill : [
      'node_modules/angular2/bundles/angular2-polyfills.js'
    ]
  },
  assets: {
    images: [
      'src/cfp/assets/img/**/*.*'
    ],
    fonts: [
      'src/cfp/assets/fonts/**/*.*',
      'node_modules/material-design-icons/iconfont/*.*'
    ],
  },
  templates: ['src/cfp/**/*.html'],
  build: {
    root : 'dist',
    images : 'dist/cfp/assets/img',
    fonts : 'dist/cfp/assets/fonts',
    scripts : 'dist/cfp/scripts',
    styles: 'dist/cfp',
  },
  urlRewrite:[
      //Rewrite for the backend calls
      '^/api/(.*)$ http://localhost:8080/api/$1 [P]',
      '^/app/(.*)$ http://localhost:8080/app/$1 [P]',
      //Rewrite for HML
      //'!\\.\\w+$ /index.html [L]'
      '^[^\\.]*$ /cfp/index.html [L]'
  ]
};

// Lint JavaScript
gulp.task('lint', () =>
  gulp.src(paths.ts)
    .pipe($.tslint())
    .pipe($.tslint.report("verbose"))
);

// Optimize images
gulp.task('images', () =>
    gulp.src(paths.assets.images)
    .pipe($.cache($.imagemin({
      progressive: true,
      interlaced: true
    })))
    .pipe(gulp.dest(paths.build.images))
    .pipe($.size({title: 'images'}))
);
gulp.task('fonts', () =>  {
  return gulp.src(paths.assets.fonts)
    .pipe(gulp.dest(paths.build.fonts));
});

// Copy all files at the root level (src)
gulp.task('copy', () =>
  gulp.src([
    'src/cfp/favicon.ico',
    '!src/cfp/**/*.html',
    'src/cfp/manifest.*'
  ], {
    dot: true
  }).pipe(gulp.dest('dist/cfp'))
    .pipe($.size({title: 'copy'}))
);

// Compile and automatically prefix stylesheets
gulp.task('styles', () => {
  const AUTOPREFIXER_BROWSERS = [
    'ie >= 10',
    'ie_mob >= 10',
    'ff >= 30',
    'chrome >= 34',
    'safari >= 7',
    'opera >= 23',
    'ios >= 7',
    'android >= 4.4',
    'bb >= 10'
  ];

  gulp.src(paths.vendor.css)
    //In Angular Material Lite we don't use the standard primary color
    .pipe($.replace('"Roboto","Helvetica","Arial",sans-serif', '"Roboto","Arial"'))
    .pipe($.replace('"Helvetica","Arial",sans-serif', '"Roboto","Arial"'))
    .pipe($.replace('Helvetica,Arial,sans-serif', '"Roboto","Arial"'))
    .pipe($.replace('"Roboto","Arial",sans-serif', '"Roboto","Arial"'))
    .pipe($.replace('63,81,181', '69,90,100'))
    .pipe($.concat('vendors.css'))
    .pipe(gulp.dest('.tmp/cfp'))
    .pipe(gulp.dest(paths.build.styles));

  // For best performance, don't add Sass partials to `gulp.src`
  return gulp.src(paths.less)
    .pipe($.sourcemaps.init())
    .pipe($.less())
    .pipe($.replace('assets/img', '../img'))
    .pipe($.replace('../../node_modules/material-design-icons/iconfont', '../fonts'))
    .pipe($.autoprefixer(AUTOPREFIXER_BROWSERS))
    //.pipe($.concat('app.css'))
    .pipe(gulp.dest('.tmp'))
    // Concatenate and minify styles
    .pipe($.if('*.css', $.minifyCss()))
    .pipe($.size({title: 'styles'}))
    .pipe($.sourcemaps.write('./'))
    .pipe(gulp.dest(paths.build.root));
});

gulp.task('ts',  () => {
  return gulp.src(paths.ts)
    .pipe($.newer('.tmp/cfp/scripts'))
    .pipe($.typescript({
      'declaration': false,
      'experimentalDecorators': true,
      'emitDecoratorMetadata': true,
      'mapRoot': '',
      'module': 'system',
      'moduleResolution': 'node',
      'noEmitOnError': true,
      'noImplicitAny': false,
      "rootDir": ".",
      "sourceMap": true,
      "sourceRoot": "/src",
      'target': 'ES5',
    })).js
    .pipe(gulp.dest('.tmp'))
    .pipe(gulp.dest(paths.build.root));
});

// Concatenate and minify JavaScript. Optionally transpiles ES2015 code to ES5.
// to enables ES2015 support remove the line `"only": "gulpfile.babel.js",` in the
// `.babelrc` file.
gulp.task('vendors', () =>
    gulp.src(paths.vendor.js)
      .pipe($.newer('.tmp/cfp/scripts'))
      .pipe($.sourcemaps.init())
      .pipe($.babel())
      .pipe($.sourcemaps.write())
      .pipe($.concat('vendor.min.js'))
      .pipe(gulp.dest('.tmp/cfp/scripts'))
      //.pipe($.uglify())
      // Output files
      .pipe($.size({title: 'scripts'}))
      .pipe($.sourcemaps.write('.'))
      .pipe(gulp.dest(paths.build.scripts))
);
gulp.task('polyfill', () =>
  gulp.src(paths.vendor.polyfill)
    .pipe($.newer('.tm/cfpp/scripts'))
    .pipe($.sourcemaps.init())
    .pipe($.babel())
    .pipe($.sourcemaps.write())
    .pipe($.concat('polyfill.min.js'))
    .pipe(gulp.dest('.tmp/cfp/scripts'))
    //.pipe($.uglify())
    // Output files
    .pipe($.size({title: 'scripts'}))
    .pipe($.sourcemaps.write('.'))
    .pipe(gulp.dest(paths.build.scripts))
);

// Scan your HTML for assets & optimize them
gulp.task('html', () => {
  return gulp.src('src/cfp/**/*.html')
    .pipe($.useref({searchPath: '{.tmp,src}'}))
    // Remove any unused CSS
    .pipe($.if('*.css', $.uncss({
      html: [
        'src/cfp/index.html'
      ],
      // CSS Selectors for UnCSS to ignore
      ignore: []
    })))

    // Concatenate and minify styles
    // In case you are still using useref build blocks
    .pipe($.if('*.css', $.minifyCss()))

    // Minify any HTML
    //TODO we have to desactivate the minification for Angular (see this problem later)
    //.pipe($.if('*.html', $.minifyHtml()))

    // Output files
    .pipe($.if('*.html', $.size({title: 'html', showFiles: true})))
    .pipe(gulp.dest('dist/cfp'));
});

// Clean output directory
gulp.task('clean', cb => del(['.tmp', 'dist/*', '!dist/cfp.git'], {dot: true}));

// Watch files for changes & reload
gulp.task('serve', ['vendors', 'polyfill', 'ts', 'styles'], () => {
  browserSync({
    notify: false,
    // Allow scroll syncing across breakpoints
    scrollElementMapping: ['main', '.mdl-layout'],
    // Run as an https by uncommenting 'https: true'
    // Note: this uses an unsigned certificate which on first access
    //       will present a certificate warning in the browser.
    // https: true,
    server: {
      baseDir: ['.tmp', 'src'],
      middleware: [
        connectModeRewrite(paths.urlRewrite)
      ]
    },
    port: 3000,
    startPath: "/cfp/index.html"
  });

  gulp.watch(['src/cfp/**/*.html'], reload);
  gulp.watch(paths.less, ['styles', reload]);
  gulp.watch(paths.ts, ['lint', 'ts']);
  gulp.watch(paths.assets.images, reload);
});


// Build and serve the output from the dist build
gulp.task('serve:dist', ['default'], () =>
  browserSync({
    notify: false,
    // Allow scroll syncing across breakpoints
    scrollElementMapping: ['main', '.mdl-layout'],
    // Run as an https by uncommenting 'https: true'
    // Note: this uses an unsigned certificate which on first access
    //       will present a certificate warning in the browser.
    // https: true,
    server: {
      baseDir: ['dist'],
      middleware: [
        connectModeRewrite(paths.urlRewrite)
      ]
    },
    port: 3001,
    startPath: "/cfp/index.html"
  })
);

// Build production files, the default task
gulp.task('default',  ['clean'], cb =>
  runSequence(
    'styles',
    ['lint', 'html', 'vendors', 'polyfill', 'ts', 'images', 'fonts', 'copy'],
    cb
  )
);

// Run PageSpeed Insights
gulp.task('pagespeed', cb =>
  // Update the below URL to the public URL of your site
  pagespeed('example.com', {
    strategy: 'mobile'
    // By default we use the PageSpeed Insights free (no API key) tier.
    // Use a Google Developer API key if you have one: http://goo.gl/RkN0vE
    // key: 'YOUR_API_KEY'
  }, cb)
);

// Load custom tasks from the `tasks` directory
// Run: `npm install --save-dev require-dir` from the command-line
// try { require('require-dir')('tasks'); } catch (err) { console.error(err); }
