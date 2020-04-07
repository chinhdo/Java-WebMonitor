package chinhdo.webmonitor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import chinhdo.mail.SmtpHelper;
import chinhdo.util.ConfigManager;
import static org.mockito.Mockito.*;
import java.net.UnknownHostException;

@RunWith(JUnit4.class)
public class NotifierTest {
    @Before
    public void before() {
        smtp = mock(SmtpHelper.class);
        config = new ConfigManager("testConfig.xml");
        target = new Notifier(config, smtp);
    }

    @Test
    public void canProcessWithoutNotification() throws UnknownHostException {
        MonitorResult result = new MonitorResult();
        result.numErrors = 0;
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.url = "test";
        urlInfo.checkStr = "test";
        urlInfo.isOK = true;
        result.urlInfos.add(urlInfo);
        target.process(result);

        verify(smtp, times(0)).send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void canProcessAndSendNotification() throws UnknownHostException {
        doNothing().when(smtp).send(any(String.class), any(String.class), any(String.class), any(String.class));

        MonitorResult result = new MonitorResult();
        result.numErrors = 1;
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.url = "test";
        urlInfo.checkStr = "test";
        urlInfo.isOK = false;
        result.urlInfos.add(urlInfo);
        target.process(result);

        verify(smtp, times(1)).send(any(String.class), any(String.class), any(String.class), any(String.class));
    }

    private ConfigManager config;
    private SmtpHelper smtp;
    private Notifier target;
}