/*
Copyright (c) 2015 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/

'use strict';

// Include Gulp & Tools We'll Use
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var del = require('del');
var runSequence = require('run-sequence');
var browserSync = require('browser-sync');
var reload = browserSync.reload;
var merge = require('merge-stream');
var path = require('path');
var fs = require('fs');
var glob = require('glob');
var gutil = require('gulp-util');
var cache = require('gulp-cache');

var AUTOPREFIXER_BROWSERS = [
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

var styleTask = function (stylesPath, srcs) {
  return gulp.src(srcs.map(function(src) {
      return path.join('dashboard', stylesPath, src);
    }))
    .pipe($.changed(stylesPath, {extension: '.css'}))
    .pipe($.autoprefixer(AUTOPREFIXER_BROWSERS))
    .pipe(gulp.dest('.tmp/' + stylesPath))
    .pipe($.if('*.css', $.cssmin()))
    .pipe(gulp.dest('static/' + stylesPath))
    .pipe($.size({title: stylesPath}));
};

// Compile and Automatically Prefix Stylesheets
gulp.task('styles', function () {
  return styleTask('styles', ['**/*.css']);
});

gulp.task('elements', ['coffee-elements', 'sass-elements'], function () {
  return styleTask('elements', ['**/*.css']);
});

// Compile SASS and Coffee
gulp.task('coffee-elements', function () {
  return gulp.src('dashboard/elements/**/*.coffee')
    .pipe($.coffee({bare: true}).on('error', gutil.log))
    .pipe(gulp.dest('static/elements'));
});

gulp.task('sass-elements', function () {
  return gulp.src('dashboard/elements/**/*.scss')
    .pipe($.sass().on('error', gutil.log))
    .pipe(gulp.dest('static/elements'));
});

// Lint JavaScript
gulp.task('jshint', function () {
  return gulp.src([
      'dashboard/scripts/**/*.js',
      'dashboard/elements/**/*.js',
      'dashboard/elements/**/*.html'
    ])
    .pipe(reload({stream: true, once: true}))
    .pipe($.jshint.extract()) // Extract JS from .html files
    .pipe($.jshint())
    .pipe($.jshint.reporter('jshint-stylish'))
    .pipe($.if(!browserSync.active, $.jshint.reporter('fail')));
});

// Optimize Images
gulp.task('images', function () {
  return gulp.src('dashboard/images/**/*')
    .pipe($.cache($.imagemin({
      progressive: true,
      interlaced: true
    })))
    .pipe(gulp.dest('static/images'))
    .pipe($.size({title: 'images'}));
});

// Copy All Files At The Root Level (dashboard)
gulp.task('copy', function () {
  var dashboard = gulp.src([
    'dashboard/*',
    '!dashboard/test',
    '!dashboard/precache.json',
    'node_modules/apache-server-configs/dist/.htaccess'
  ], {
    dot: true
  }).pipe(gulp.dest('static'));

  var bower = gulp.src([
    'bower_components/**/*'
  ]).pipe(gulp.dest('static/bower_components'));

  var elements = gulp.src(['dashboard/elements/**/*.html'])
    .pipe(gulp.dest('static/elements'));

  var swBootstrap = gulp.src(['bower_components/platinum-sw/bootstrap/*.js'])
    .pipe(gulp.dest('static/elements/bootstrap'));

  var swToolbox = gulp.src(['bower_components/sw-toolbox/*.js'])
    .pipe(gulp.dest('static/sw-toolbox'));

  var vulcanized = gulp.src(['dashboard/elements/elements.html'])
    .pipe($.rename('elements.vulcanized.html'))
    .pipe(gulp.dest('static/elements'));

  return merge(dashboard, bower, elements, vulcanized, swBootstrap, swToolbox)
    .pipe($.size({title: 'copy'}));
});

// Copy Web Fonts To Dist
gulp.task('fonts', function () {
  return gulp.src(['dashboard/fonts/**'])
    .pipe(gulp.dest('static/fonts'))
    .pipe($.size({title: 'fonts'}));
});

// Scan Your HTML For Assets & Optimize Them
gulp.task('html', function () {
  var assets = $.useref.assets({searchPath: ['.tmp', 'dashboard', 'static']});

  return gulp.src(['dashboard/**/*.html', '!dashboard/{elements,test}/**/*.html'])
    // Replace path for vulcanized assets
    .pipe($.if('*.html', $.replace('elements/elements.html', 'elements/elements.vulcanized.html')))
    .pipe(assets)
    // Concatenate And Minify JavaScript
    .pipe($.if('*.js', $.uglify({preserveComments: 'some'})))
    // Concatenate And Minify Styles
    // In case you are still using useref build blocks
    .pipe($.if('*.css', $.cssmin()))
    .pipe(assets.restore())
    .pipe($.useref())
    // Minify Any HTML
    .pipe($.if('*.html', $.minifyHtml({
      quotes: true,
      empty: true,
      spare: true
    })))
    // Output Files
    .pipe(gulp.dest('static'))
    .pipe($.size({title: 'html'}));
});

// Vulcanize imports
gulp.task('vulcanize', function () {
  var DEST_DIR = 'static/elements';

  return gulp.src('static/elements/elements.vulcanized.html')
    .pipe($.vulcanize({
      dest: DEST_DIR,
      strip: true,
      inlineCss: true,
      inlineScripts: true
    }))
    .pipe(gulp.dest(DEST_DIR))
    .pipe($.size({title: 'vulcanize'}));
});

// Generate a list of files that should be precached when serving from 'dist'.
// The list will be consumed by the <platinum-sw-cache> element.
gulp.task('precache', function (callback) {
  var dir = 'static';

  glob('{elements,scripts,styles}/**/*.*', {cwd: dir}, function(error, files) {
    if (error) {
      callback(error);
    } else {
      files.push('index.html', './', 'bower_components/webcomponentsjs/webcomponents.min.js');
      var filePath = path.join(dir, 'precache.json');
      fs.writeFile(filePath, JSON.stringify(files), callback);
    }
  });
});

// Clean Output Directory
gulp.task('clean', function() {
  del.bind(null, ['.tmp', 'static']);
  cache.clearAll();
});

// Watch Files For Changes & Reload
gulp.task('serve', ['styles', 'elements', 'images'], function () {
  browserSync({
    notify: false,
    // Run as an https by uncommenting 'https: true'
    // Note: this uses an unsigned certificate which on first access
    //       will present a certificate warning in the browser.
    // https: true,
    server: {
      baseDir: ['.tmp', 'dashboard'],
      routes: {
        '/bower_components': 'bower_components'
      }
    }
  });

  gulp.watch(['dashboard/**/*.html'], reload);
  gulp.watch(['dashboard/styles/**/*.{css}'], ['styles', reload]);
  gulp.watch(['dashboard/elements/**/*.scss'], ['elements', reload]);
  gulp.watch(['dashboard/elements/**/*.coffee'], ['coffee-elements', reload]);
  gulp.watch(['dashboard/{scripts,elements}/**/*.js'], ['jshint']);
  gulp.watch(['dashboard/images/**/*'], reload);
});

// Build and serve the output from the static build
gulp.task('serve:static', ['default'], function () {
  browserSync({
    notify: false,
    // Run as an https by uncommenting 'https: true'
    // Note: this uses an unsigned certificate which on first access
    //       will present a certificate warning in the browser.
    // https: true,
    server: 'static'
  });
});

// Build Production Files, the Default Task
gulp.task('default', ['clean'], function (cb) {
  runSequence(
    ['copy', 'styles'],
    'elements',
    ['jshint', 'images', 'fonts', 'html'],
     'precache',
    cb);
});

// Load tasks for web-component-tester
// Adds tasks for `gulp test:local` and `gulp test:remote`
try { require('web-component-tester').gulp.init(gulp); } catch (err) {}

// Load custom tasks from the `tasks` directory
try { require('require-dir')('tasks'); } catch (err) {}
