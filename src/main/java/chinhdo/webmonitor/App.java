package chinhdo.webmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import chinhdo.mail.SmtpNotifier;
import chinhdo.util.Helper;

public class App 
{
    public static void main( final String[] args ) throws IOException, ConfigurationException, NoSuchAlgorithmException, KeyManagementException
    {  	
    	log.info("====> Starting application (V1.1.0.18). Datetime: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" + ".")).format(new Date()));
		log.debug("Current folder: " + Helper.GetCurrentDir());
		    	
    	// This is to ignore invalid cert issues
    	final TrustManager trm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {return null;}
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {}
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {}
        };
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { trm }, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		   	
		XMLConfiguration config;
    	try {
			config = new XMLConfiguration("config.xml");
		}
		catch (final ConfigurationException ex) {
			log.error("*ERROR* config.xml not found. See README.md for instructions.");
			return;
		}

		log.debug("Config file: " + config.getBasePath());

		final HashMap<String, String> data = new HashMap<String, String>();
		final List<Object> urls = config.getList("urlInfos.urlInfo.url");
		final List<Object> checks = config.getList("urlInfos.urlInfo.check");
		log.debug("Number of URLs: " + urls.size());
		for (int i = 0; i < urls.size(); i++) {
			final String url = urls.get(i).toString();
			final String check = checks.get(i).toString();
			data.put(url, check);
		}

		final StringBuffer report = new StringBuffer();
		final StringBuffer failedUrls = new StringBuffer();
		boolean hasErrors = false;
		for (final String url : data.keySet()) {

			final long started = System.currentTimeMillis();
			String html = null;
			Exception getUrlException = null;
			try {
				html = GetUrl(url);
			} catch (final Exception e) {
				getUrlException = e;

				log.debug("Failed to get " + url + ": " + e.getStackTrace());
			}

			final float elapsed = System.currentTimeMillis() - started;

			if (getUrlException == null) {
				if (html.indexOf(data.get(url)) >= 0) {
					final NumberFormat formatter = new DecimalFormat("#0.00");
					AppendReport(report,
							url + ": OK (" + html.length() + " bytes, " + formatter.format(elapsed / 1000) + "s)");
				} else {
					hasErrors = true;
					AppendReport(report, url + ": FAILED (cannot find checkstring '" + data.get(url) + "')");
					if (failedUrls.length() > 0)
						failedUrls.append(", ");
					failedUrls.append(url);

					log.debug("Response: " + html);
				}
			} else {
				hasErrors = true;

				AppendReport(report, url + ": FAILED (" + getUrlException.getMessage() + ")");
				if (failedUrls.length() > 0)
					failedUrls.append(", ");
				failedUrls.append(url);
			}
		}

		// Send alert
		if (hasErrors) {
			log.debug("Sending alert.");

			final String subject = "Check(s) failed for url(s): " + failedUrls.toString();
			final StringBuffer body = new StringBuffer(report.toString());
			body.append(NEWLINE);
			body.append("Sent from " + Helper.GetComputerName() + ". Program location: " + Helper.GetCurrentDir());

			log.debug("Subject: " + subject);
			log.debug("Body: [" + body + "]");

			final String smtpHost = config.getString("smtp.host");
			final int smtpPort = config.getInt("smtp.port");
			final String smtpLogin = config.getString("smtp.login");
			final String smtpPwd = config.getString("smtp.pwd");
			final String notifyEmails = config.getString("smtp.notifyEmails");

			final SmtpNotifier mailer = new SmtpNotifier(smtpHost, smtpPort, smtpLogin, smtpPwd, notifyEmails);
			// mailer.notify(subject, body.toString());

			// TODO Gmail.SendMail("WebMonitor", "chinhdo@gmail.com", "Failed to contact " + failedUrls.toString(), body.toString());
		}

		log.debug("Exiting.");
    }

	private static Logger  log = Logger.getLogger("chinhdo.webmonitor");
    private static String NEWLINE = System.getProperty("line.separator");

    private static void AppendReport(final StringBuffer report, final String msg) {
    	report.append(msg);
    	report.append(NEWLINE);
    	log.debug(msg);
    }

	private static String GetUrl(final String urlString) throws IOException {
		final URL url = new URL(urlString);
		final InputStream is = url.openStream();
		int ptr = 0;
		final StringBuffer buffer = new StringBuffer();
		while ((ptr = is.read()) != -1) {
		    buffer.append((char)ptr);
		}
		
		return buffer.toString();
	}
}