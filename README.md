![Travis CI](https://travis-ci.org/mix-it/cesar.svg?branch=master)

# Cesar

Cesar helps to manage a conference about computer sciences. Cesar is used for [Mix-IT](http://www.mix-it.fr/) (Lyon, France). The staging environment is available [here](http://cesar.cfapps.io/).
![Mix-IT](client-ng1/src/app/assets/logo/logo-mixit.png)

The project lifecycle is managed with [gradle](https://gradle.org/). You don't need to install Gradle on your host because this project use the [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).  

## Project organization 

* [common](common/README.md) Common module with database management
* [client-ng1](client-ng1/README.md) UI Angular 1.4.x
* [client-ng2](client-ng2/README.md) UI Angular 2.0.x (todo)
* [server](server/README.md) Backend

## Launch the webapp

This project uses [Spring Boot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/). To launch the webapp you can use this command
```
./gradlew bootRun
```
, start a web browser and visit [http://localhost:8080](http://localhost:8080).  

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
