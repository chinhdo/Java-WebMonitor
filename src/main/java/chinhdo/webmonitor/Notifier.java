package chinhdo.webmonitor;

import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import chinhdo.mail.SmtpHelper;
import chinhdo.util.ConfigManager;
import chinhdo.util.Helper;

/** Sends out notifications */
public class Notifier {
    /** Constructor */
    public Notifier(ConfigManager config, SmtpHelper smtp) {
        this.config = config;
        this.smtp = smtp;
    }

    /**
     * Process monitoring result and sends notifications if required
     * 
     * @throws UnknownHostException
     */
    public void process(MonitorResult result) throws UnknownHostException {
        if (result.numErrors > 0) {
            final String subject = "Java WebMonitor - " + result.numErrors + " error(s)";
            
            final StringBuffer body = new StringBuffer();
            body.append("<ul>");
            for (UrlInfo info : result.urlInfos) {
                if (info.isOK) {
                    body.append(String.format("<li>Health check for %s successful (elapsed: %d)</li>", info.url, info.elapsedMs));
                }
                else {
                    body.append(String.format("<li>Health check for %s failed: %s</li>", info.url, info.error));
                }
            }
            body.append("</ul>");
            
            body.append("<p>Sent from " + Helper.GetComputerName() +
                ". Program location: " + Helper.GetCurrentDir() + "</p>");


            final String fromAddr = config.getString("notification.from");
            final String toEmails = config.getString("notification.to");

            log.info("Sending email notification.");
            smtp.send(fromAddr, toEmails, subject, body.toString());
        }
    }

    private static Logger  log = Logger.getLogger("chinhdo.webmonitor.Notifider");
    private ConfigManager config;
    private SmtpHelper smtp;
}