# Cesar UI

This module holds all web resources (Js, CSS, images...). We use several tools for its own build system

* [Node.js and npm](https://nodejs.org/) : downloads all the required JS libraries
* [Gulp](http://gulpjs.com/) : builds system used to package the project. The process is defined in the main file [gulpfile.old](gulpfile.old) and in all its sub [config files](gup)
* [Gradle](https://gradle.org/) : builds a web archive which will be deployed in the final webapp

This module use Angular V1

## Life cycle Gulp

We use gulp to manage the lifecycle of the web applications 

To build the project you can lauch `gulp`. By default the tasks `clean`, `test` and `build` are launched

The main tasks are defined in the gulpfile.old and in the subfiles available in `/gulp`

* `default` : launch tasks `clean`, `test` and `build`
* `build` : package app for dev and prod
* `bundle` : prepare assets used in dev and prod
* `serve` : start a web server and launch the webapp
* `test` : launch all the unit tests
* `test:watch` : launch all the unit tests with Karma watch mode

## Develop UI

When you develop the web application you can launch an http server with this gulp task

```
gulp serve
```

Several watchers rerun the server when you change files (JS, HTML, Less...). See the gulp task [watch](gulp/dev.js). 

When you want to package the web resources for the final webapp (not needed in development) you can launch the command

```
../gradlew build
```