WebMonitor
==========

A command line Java app to monitor web sites and send out email notifications. Schedule with crontab -e.

To build:
mvn clean install

To make one single JAR including dependencies:
mvn assembly:single

To run on Windows:
java -cp target/webmonitor-1.0.jar;target/lib/* chinhdo.webmonitor.App

To run on Unix:
/usr/local/bin/java -cp .:webmonitor-1.0.jar:lib/* chinhdo.webmonitor.App

To add/modify list of web sites, check strings, copy config.xml to app folder. Your copy will be used instead of the one built into the Jar.

