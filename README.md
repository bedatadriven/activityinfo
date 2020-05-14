
ActivityInfo 2.7
================

ActivityInfo is an online humanitarian project monitoring tool,
which helps humanitarian organizations to collect, manage, map
and analyze indicators. ActivityInfo 2.0 was developed to
simplify reporting and allow for real time monitoring.

This repository is an archive of ActivityInfo 2.7.

Visit http://www.activityinfo.org to learn more.

Latest Version
--------------

ActivityInfo 4.0 is the latest version.

Visit https://www.activityinfo.org/signUp to create an account on
ActivityInfo.org and get started.



Building & Running ActivityInfo 2.7
-----------------------------------

### Prerequisites

Building ActivityInfo requires:
* Java JDK 1.8 (may not build on later versions)
* Apache Maven 3.3.9
* MySQL 5.6

### Test Suite

To run the test suite, first create an empty test database in a
locally-running MySQL database:

    mysql -uroot -proot -e "CREATE DATABASE activityinfo2_test"

Then run:

    mvn -Preset-test-db test


### Running Locally

Set up a local MySQL database:

    mysql -uroot -proot -e "CREATE DATABASE activityinfo2"
    mysql -uroot -proot activityinfo2 < activityinfo-bare.sql

Then start Jetty:

    mvn jetty:run-war

