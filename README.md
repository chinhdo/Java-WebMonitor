Java WebMonitor - A command line Java app to monitor web sites and send out email notifications. Schedule with crontab -e or Windows Scheduled Tasks.

# Quick Start (Unix/Linux)

In folder with webmonitor-1.1.jar:

Copy example.config.xml to config.xml and configure put in your SMTP server info and the URLs you want to monitor. The app will look for the string specified in the *check* attribute in the response.

```xml
<?xml version="1.0" encoding="ISO-8859-1" ?>
<config>
  <notification>
    <from>Web Monitor &lt;no-reply@YOUR_DOMAIN.com&gt;</from>
    <to>YOUR_TO_ADDRESS</to>
  </notification>
  <smtp>
    <host>SMTP_HOST</host>
    <port>465</port>
    <login>SMTP_LOGIN</login>
    <pwd>SMTP_PASSWORD</pwd>
    <notifyEmails>NOTIFICATION_EMAIL</notifyEmails>
  </smtp>
  <urlInfos>
    <urlInfo><url>https://YOUR_URL</url><check>CHECK_STRING</check></urlInfo>
  </urlInfos>
</config>
```


```
java -cp webmonitor-1.1.jar:target/lib/* chinhdo.webmonitor.App
```


# Frequently Asked Questions

## How to I build the app?

```
mvn clean install
```

or

```
mvn -B package --file pom.xml
```

# How to run app on Unix/Linux:
```
java -cp .:webmonitor-1.1.jar:lib/* chinhdo.webmonitor.App
```

## How do I run on Windows?

```
java -cp webmonitor-1.1.jar;lib/* chinhdo.webmonitor.App
```

Or you can run with Maven from the project
```
mvn exec:java -Dexec.mainClass="chinhdo.webmonitor.App"
```

## How do I crate a single JAR?

```
mvn assembly:single
```

## How do I get help?

Create an issue if you need help or contact [me](https://github.com/chinhdo).