package chinhdo.util;

import java.net.UnknownHostException;

import org.junit.Test;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HelperTest extends TestCase {

    @Test
    public void CanGetComputerName() throws UnknownHostException {
        assertNotNull(Helper.GetComputerName());
    }

    @Test
    public void canCreate() {
        Helper helper = new Helper();
        assertNotNull(helper);
    }

    @Test
    public void CanGetCurrentDir() {
        assertNotNull(Helper.GetCurrentDir());
    }

}