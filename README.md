WebMonitor
==========

A command line Java app to monitor web sites and send out email notifications. Schedule with cron.

To make one single JAR including dependencies: mvn assembly:single. TODO: This stopped working for some reason.

To run on Windows:
java -cp target/webmonitor-0.0.1-SNAPSHOT.jar;target/lib/* chinhdo.webmonitor.App
java -cp target/webmonitor-1.0-jar-with-dependencies.jar chinhdo.webmonitor.App


To run in Unix:
/usr/local/bin/java -cp .:webmonitor-0.0.1.jar:lib/* chinhdo.webmonitor.App

mvn clean install