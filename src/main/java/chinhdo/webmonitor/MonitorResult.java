package chinhdo.webmonitor;

import java.util.LinkedList;
import java.util.List;

/** Monitoring result */
public class MonitorResult {
    public MonitorResult() {
        urlInfos = new LinkedList<UrlInfo>();
    }

    /** Number of errors */
    public int numErrors = 0;

    /** List of individual URL results */
    public List<UrlInfo> urlInfos;
}