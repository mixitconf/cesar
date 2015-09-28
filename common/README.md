# Cesar common

This module helps to prepare the different resources used during the development phase

* [Flyway](http://flywaydb.org/) : manages the database migration
* [Gradle](https://gradle.org/) : pilots all the tasks

## Database

During the development phase, we use an H2 databse. All the SQL scripts to create the database are in the [resources](src/main/resources/db/migration)

To migrate the database used this command

```
../gradlew flywayMigrate
```

If you want have more informations on your database version launch

```
../gradlew flywayInfo
```

And if you want to delete the database you can used

```
../gradlew flywayClean
```

If you want to use a mysql database you can use this command

```
../gradlew  
```

If you want to use a mysql database you can use this command

```
../gradlew flywayClean flywayMigrate -Pdatabase=mysql
```
