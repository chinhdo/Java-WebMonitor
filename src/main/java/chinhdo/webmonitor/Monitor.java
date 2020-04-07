package chinhdo.webmonitor;
import java.util.List;
import chinhdo.util.ConfigManager;
import org.apache.log4j.Logger;

/** Monitor web URLs */
public class Monitor {
    /** Constructor */
    public Monitor(ConfigManager config, UrlHelper urlHelper) {
        this.config = config;
        this.urlHelper = urlHelper;
    }

    /** Start checking URLs */
    public MonitorResult start() {
        MonitorResult result = new MonitorResult();

        final List<Object> urls = config.getList("urlInfos.urlInfo.url");
        final List<Object> checks = config.getList("urlInfos.urlInfo.check");

        for (int i = 0; i < urls.size(); i++) {
            UrlInfo info = new UrlInfo();
            info.url = urls.get(i).toString();
            info.checkStr = checks.get(i).toString();
            info.isOK = false;
            info.length = 0;
    
            log.debug("Checking URL " + info.url + ".");
            final long requestStarted = System.currentTimeMillis();
            String html = null;
            try {
                html = urlHelper.get(info.url);
                if (html.indexOf(info.checkStr) >= 0) {
                    info.length = html.length();
                    info.isOK = true;
                } else {
                    info.error = "Cannot find check string '" + info.checkStr + "'.";
                    log.debug("Response: " + html);
                }
            }
            catch (final Exception e) {
                info.error = e.getMessage();
            }
            info.elapsedMs = System.currentTimeMillis() - requestStarted;
            result.urlInfos.add(info);

            String logMsg = " url=\"" + info.url + "\" len=" +
                info.length + " elapsed=" + info.elapsedMs + (info.isOK ? "" : " error=" + info.error);

            if (info.isOK) {
                log.info(logMsg);
            }
            else {
                log.error(logMsg);
                result.numErrors ++;
            }
		}

        return result;
    }

    private static Logger  log = Logger.getLogger("chinhdo.Monitor");
    private ConfigManager config;
    private UrlHelper urlHelper;
}