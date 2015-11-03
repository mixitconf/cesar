![Travis CI](https://travis-ci.org/mix-it/cesar.svg?branch=master)

# Cesar

Cesar est un projet permettant de générer une application permettant de gérer une conférence informatique. Cesar a été développée dans le cadre de la conférence [Mix-IT](http://www.mix-it.fr/)

![Mix-IT](client-ng1/src/app/assets/logo/logo-mixit.png)

The project lifecycle is managed with [gradle](https://gradle.org/). You don't need to install Gradle on your host because this project use the [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).  

## Project organization 

* [common](common/README.md) Common module with database management
* [client-ng1](client-ng1/README.md) UI Angular 1.4.x
* [client-ng2](client-ng2/README.md) UI Angular 2.0.x
* [server](server/README.md) Backend

## Launch the webapp

This project uses [Spring Boot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/). To launch the webapp you can use this command
```
./gradlew bootRun
```
, start a web browser and visit [http://localhost:8080](http://localhost:8080). For the moment we have 2 web clients written in Angular 1 and Angular 2. To used Angular 2 one you need to define a parameter 
```
./gradlew bootRun -Pclient=ng2 -Pdatabase=mysql
```

## Setup DB 

Go in the module [common](common/README.md) 


## Licence

The project is open source on the MIT License (MIT)

    Copyright (c) <year> <copyright holders>

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
    
## Working with the code

You have to clone the main project

```
git clone https://github.com/mix-it/cesar.git
```

After you just have to build the project `./gradlew build`. By default we don't commit any metadata linked to an IDE but all of us use IntelliJ Idea. You can contribute by [Pull requests](https://help.github.com/articles/using-pull-requests/)
