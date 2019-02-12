'use strict';

var gulp = require('gulp');
var sass = require('gulp-sass');
var cssmin = require('gulp-cssmin');
var rename = require('gulp-rename');

var path = 'magnolia-module-geologix/src/main/resources/geologix/webresources';

// convert SCSS to CSS
gulp.task('sass', function () {
    return gulp.src([path + '/scss/*.scss', '!' + path + '/scss/normalize.scss' ])
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest(path + '/css'));
});

// watch task
gulp.task('watch', ['sass'], function () {
    gulp.watch(path + '/scss/*.scss', ['sass']);
});

// minify CSS to MIN.CSS
gulp.task('minify', function () {
    gulp.src([path + '/css/*.css', '!' + path + '/css/normalize.css', '!' + path + '/css/*.min.css'])
        .pipe(cssmin())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(path + '/css'));
});

// define 'default' task
gulp.task('default', ['sass', 'minify', 'watch']);
