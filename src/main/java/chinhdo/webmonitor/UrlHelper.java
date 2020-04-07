package chinhdo.webmonitor;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import chinhdo.util.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/** Helper class with methods to request URLs, etc. */
public class UrlHelper {

    /**
     * Constructor
     * 
     * @throws NoSuchAlgorithmException
     */
    public UrlHelper(ConfigManager config) {
        if (config.getBoolean("ignoreCertErrors")) {
            // This is to ignore invalid cert issues
            final TrustManager trm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {}
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {}
            };
            SSLContext sc;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[] { trm }, null);
            } catch (NoSuchAlgorithmException|KeyManagementException e) {
                throw new RuntimeException(e);
            }

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
    }

    /** Get a URL */
    public String get(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "ChinhDo.WebMonitor");
        con.setReadTimeout(15000);
        con.setConnectTimeout(15000);
        con.setDoOutput(false);

        // final URL url = new URL(urlString);
        final InputStream is = url.openStream();
        int ptr = 0;
        final StringBuffer buffer = new StringBuffer();
        while ((ptr = is.read()) != -1) {
            buffer.append((char)ptr);
        }
        
        return buffer.toString();
    }
}