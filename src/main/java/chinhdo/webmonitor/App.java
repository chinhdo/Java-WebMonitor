package chinhdo.webmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import chinhdo.mail.Gmail;
import chinhdo.util.Helper;

public class App 
{
    public static void main( String[] args ) throws IOException, ConfigurationException, NoSuchAlgorithmException, KeyManagementException
    {  	
    	log.info("====> Starting application (V1.0.0.17). Datetime: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" + ".")).format(new Date()));
    	log.debug("Current folder: " + Helper.GetCurrentDir());
    	
    	// This is to ignore invalid cert issues
    	TrustManager trm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { trm }, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());   
    	
    	HashMap<String, String> data = new HashMap<String, String>();
    	XMLConfiguration config = new XMLConfiguration("config.xml");
    	
		log.debug(config.getBasePath());
    	
    	Object prop = config.getProperty("urls.url.urlString");
    	if(prop instanceof Collection)
    	{
			Collection c = (Collection) prop;
    		log.debug("Number of URLs: " + c.size());
    		for (int i=0; i<c.size(); i++) {
    			String url = config.getString("urls.url(" + i + ").urlString");
    			String checkString = config.getString("urls.url(" + i + ").checkString");
    			data.put(url, checkString);
    		}
    	}
		
		StringBuffer report = new StringBuffer();
		StringBuffer failedUrls = new StringBuffer();
		boolean hasErrors = false;
		for (String url : data.keySet()) {
			
			long started = System.currentTimeMillis();
			String html = null;
			Exception getUrlException = null;
			try {				
				html = GetUrl(url);
			}
			catch (Exception e) {
				getUrlException = e;
				
				log.debug(e.getStackTrace());
			}
			
			float elapsed = System.currentTimeMillis() - started;
			
			if (getUrlException == null) {
				if (html.indexOf(data.get(url)) >= 0) {
					NumberFormat formatter = new DecimalFormat("#0.00");
					AppendReport(report, url + ": OK (" + html.length() + " bytes, " + formatter.format(elapsed/1000) + "s)");
				}
				else {
					hasErrors = true;
					AppendReport(report, url + ": FAILED (cannot find checkstring '" + data.get(url) + "')");
					if (failedUrls.length()>0) failedUrls.append(", ");
					failedUrls.append(url);
					
					log.debug(html);
				}
			}
			else {
				hasErrors = true;
				
				AppendReport(report, url + ": FAILED (" + getUrlException.getMessage() + ")");				
				if (failedUrls.length()>0) failedUrls.append(", ");
				failedUrls.append(url);				
			}
		}
		
		// Check port
		/*
		String hostName = "chinhdo.dyndns.info";
		Integer port = 8080;
		if (TestPort(hostName, port)) {
			log.debug(hostName + ":" + port + ": OK");
		}
		else {
			hasErrors = true;
			failedUrls.append(hostName);
			AppendReport(report, "Cannot connect to " + hostName + ":" + port + ".");
		}
		*/
		
		// Send alert
		if (hasErrors) {
			log.debug("Sending alert.");
			
			String subject = "Check(s) failed for url(s): " + failedUrls.toString();
			StringBuffer body = new StringBuffer(report.toString());
			body.append(NEWLINE);
			body.append("Sent from " + Helper.GetComputerName() + ". Program location: " + Helper.GetCurrentDir());
			
			log.debug("Subject: " + subject);
			log.debug("Body: [" + body + "]");

			Gmail.SendMail("WebMonitor", "chinhdo@gmail.com", "Failed to contact " + failedUrls.toString(), body.toString());
		}

		log.debug("Exiting.");
    }

	private static Logger  log = Logger.getLogger("chinhdo.webmonitor");
    private static String NEWLINE = System.getProperty("line.separator");

    private static void AppendReport(StringBuffer report, String msg) {
    	report.append(msg);
    	report.append(NEWLINE);
    	log.debug(msg);
    }

	private static String GetUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		InputStream is = url.openStream();
		int ptr = 0;
		StringBuffer buffer = new StringBuffer();
		while ((ptr = is.read()) != -1) {
		    buffer.append((char)ptr);
		}
		
		return buffer.toString();
	}
	
	private static boolean TestPort(String host, Integer port) {
		Socket socket = new Socket();
		InetSocketAddress endPoint = new InetSocketAddress( host, port);
		
		boolean ret = false;
		
		if (endPoint.isUnresolved()) {
			ret = false;
		}
		else try { 
            socket.connect(endPoint , 30000);
            ret = true;

        } catch( IOException ioe ) {
        	log.error("Failed to connect to port.", ioe);

        } finally {
            if ( socket != null ) try {
                socket.close();
            } catch( IOException ioe ) {}
        } 
		
		return ret;
	}
}