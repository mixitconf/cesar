# Cesar common

This module helps to prepare the different resources used during the development phase

* [Flyway](http://flywaydb.org/) : manages the database migration
* [Gradle](https://gradle.org/) : pilots all the tasks

## Database used in the build process

We use Flyway to manage the databases. All the SQL scripts to create the database are in the [resources](src/main/resources/db/migration). In the default build lifecycle, we use an H2 database 

To avoid to launch all the Gradle phases go in the directory ~/Workspace/cesar and to manage your database used these commands


To migrate your schema 

```
../gradlew flywayMigrate
```

If you want to have more informations on your database version launch

```
../gradlew flywayInfo
```

And if you want to delete the database you can used

```
../gradlew flywayClean
```

If you want to use a mysql database you can use this command

```
../gradlew flywayClean flywayMigrate -Pdatabase=mysql
```

If you want to use your own database params to launch flyway you can use
```
../gradlew flywayClean flywayMigrate -Dflyway.url=jdbc:mysql://localhost:3306/mixit -Dflyway.user=mixit -Dflyway.password=mixit -Dflyway.locations=classpath:db/migration/mysql
```

## Database used when you launch the app

By default the app needs a MySQL DB. If you want to use the H2 database you need to override the Spring Boot configuration and used these args when you launch the jar
```java
./gradlew bootRun --spring.datasource.driver-class-name=org.h2.Driver --spring.datasource.url=jdbc:h2:file:cesar --spring.datasource.username=sa --spring.datasource.password=
```

The database is not created automatically when you start the application. If you want to generate the database you can use flyway or add these arguments. 
```                                                                                                                ./gradlew bootRun --spring.datasource.driver-class-name=org.h2.Driver --spring.datasource.url=jdbc:h2:file:cesar --spring.datasource.username=sa --spring.datasource.password= --spring.jpa.hibernate.ddl-auto=create --spring.jpa.hibernate.naming_strategy: org.hibernate.cfg.EJB3NamingStrategy
```

If you want to use MySQL, install a version > 5.x


Connect : 
```
mysql -u root -p
```
Then
```
CREATE DATABASE mixit;
CREATE USER 'mixit'@'localhost' IDENTIFIED BY 'mixit';
GRANT ALL on mixit.* TO 'mixit'@'localhost'
```

If you want to ovverride the default MySQL database you can use environment variables
```
export CESAR_DATABASE_DRIVER=com.mysql.jdbc.Driver
export CESAR_DATABASE_HOST=localhost
export CESAR_DATABASE_PORT=3306
export CESAR_DATABASE_SCHEMA=mixit
export CESAR_DATABASE_USERNAME=mixit
export CESAR_DATABASE_PASSWORD=mixit
export
```
