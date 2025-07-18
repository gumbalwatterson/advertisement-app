# Advertisement-app
Application used for publication of announcements.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Database](#database)
* [Scheduler](#scheduler)
* [Setup](#setup)

## General info

Application has four endpoint doing crud operations over announcements.
Internally it uses two cache mechanism to avoid unnecessary calls to database. First cache is the spring internal cache 
enabled by spring annotations. Its purpose is to save in its cache announcements object and pass them for get requests. 
Second cache is to save number of views for every get request to reduce number of calls and updates to database. 
Both caches are clear after scheduler update database with cache data. Fixed delay of scheduler can be tune by property:
ann.app.scheduler.delay.
By default application uses H2 database creating table after start up.


## Technologies
Main technologies:
* Java 21
* Spring Boot 3.5.3
* Lombok 1.18.38
* H2 Database 2.3.232

## Database

### H2 Database
Database which contains announcements entities.
* console path is configured at /console
* schema created after start up
* running test task script will fill test database with data

### Scheduler
It is configured in properties file. Its fixed delay time must be tuned based on resources and individual requirements.
Scheduler is working as a single thread which responsibility is to flush cached data to database. This operation should not
be done often to avoid performance issues.

## Setup
Project repo link: https://github.com/gumbalwatterson/advertisement-app

Setup details:
* clone project from above link
* h2 database schema will be created after start up
* we can start application locally using: ./gradlew bootRun

