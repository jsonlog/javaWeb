var gulp = require('gulp');
var connect = require('gulp-connect');

gulp.task('webserver', function () {
    connect.server({
        port: 1234
    });
});

gulp.task('default', ['webserver']);