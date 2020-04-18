package chinhdo.webmonitor;

import static org.junit.Assert.*;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import chinhdo.util.ConfigManager;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class MonitorTest {
    private final Monitor target;
    private final UrlHelper urlHelper;
    private final ConfigManager config;

    public MonitorTest() throws ConfigurationException {
        config = mock(ConfigManager.class);
        urlHelper = mock(UrlHelper.class);
        this.target = new Monitor(config, urlHelper);
    }

    @Test
    public void CanCreate() {
        assertNotNull(target);
    }

    @Test
    public void CanMonitorUrls() throws IOException {
        final List<Object> urls = Arrays.asList("https://test1.com", "https://test2.com");
        final List<Object> checks = Arrays.asList("test1", "test2");
        when(config.getList("urlInfos.urlInfo.url")).thenReturn(urls);
        when(config.getList("urlInfos.urlInfo.check")).thenReturn(checks);
        when(urlHelper.get("https://test1.com")).thenReturn("test1");
        when(urlHelper.get("https://test2.com")).thenReturn("bad");

        final MonitorResult monitorResult = target.start();
        final List<UrlInfo> infos = monitorResult.urlInfos;
        assertEquals(2, infos.size());
        assertTrue(infos.get(0).isOK);
        assertFalse(infos.get(1).isOK);

        verify(config, times(1)).getList("urlInfos.urlInfo.url");
        verify(config, times(1)).getList("urlInfos.urlInfo.check");
    }
}