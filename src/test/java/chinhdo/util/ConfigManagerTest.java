package chinhdo.util;

import org.junit.Before;
import org.junit.Test;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConfigManagerTest extends TestCase {
    public static TestSetup suite() {
        final TestSetup setup = new TestSetup(new TestSuite(ConfigManagerTest.class)) {
            protected void setUp(  ) throws Exception {
                // do your one-time setup here!
            }
            protected void tearDown(  ) throws Exception {
                // do your one-time tear down here!
            }
        };
        return setup;
    }

    @Before
    public void before() {
        target = new ConfigManager("testConfig.xml");
    }

    @Test
    public void canGetBoolean() {
        assertFalse(target.getBoolean("non-existent-key"));
    }

    @Test
    public void canGetInt() {
        assertEquals(465, target.getInt("smtp.port"));
    }

    @Test
    public void canGetList() {
        assertEquals("https://www.test.com", target.getList("urlInfos.urlInfo.url").get(0));
    }

    @Test
    public void canGetString() {
        assertEquals("", target.getString("smtp.host"));
    }

    private ConfigManager target;
}