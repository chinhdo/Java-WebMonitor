To make one single JAR including dependencies: mvn assembly:single

To run on Windows:
java -cp target/webmonitor-0.0.1-SNAPSHOT.jar;target/lib/* chinhdo.webmonitor.App

To run in Unix:
/usr/local/bin/java -cp .:webmonitor-0.0.1.jar:lib/* chinhdo.webmonitor.App

mvn clean install