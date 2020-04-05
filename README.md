Java WebMonitor - A command line Java app to monitor web sites and send out email notifications. Schedule with crontab -e or Windows Scheduled Tasks.

# Quick Start (Unix/Linux)

In folder with webmonitor-1.1.jar:

Copy example.config.xml to config.xml and configure put in your SMTP server info and the URLs you want to monitor. The app will look for the string specified in the *check* attribute in the response.

```xml
  <smtp>
    <host>your_smtp_host_here</host>
    <port>465</port>
    <login>your_smtp_login</login>
    <pwd>your_smtp_password</pwd>
    <notifyEmails>notification email address</notifyEmails>
  </smtp>
  <urlInfos>
    <urlInfo>
      <url>https://your_URL_here</url>
      <check>your_check_string_here</check>
    </urlInfo>
  </urlInfos>
```


```
java -cp webmonitor-1.1.jar:target/lib/* chinhdo.webmonitor.App
```


# Frequently Asked Questions

## How to I build the app?

```
mvn clean install
```

## How do I crate a single JAR?

```
mvn assembly:single
```

## How do I run on Windows?

```
java -cp target/webmonitor-1.1.jar;target/lib/* chinhdo.webmonitor.App
```

or 
```
mvn exec:java -Dexec.mainClass="chinhdo.webmonitor.App"
```

# How to run app on Unix/Linux:
```
/usr/local/bin/java -cp .:webmonitor-1.0.jar:lib/* chinhdo.webmonitor.App
```