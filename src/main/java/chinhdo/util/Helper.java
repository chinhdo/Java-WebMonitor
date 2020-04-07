package chinhdo.util;
import java.io.File;
import java.net.*;

/** Helper functions */
public class Helper {
	public static String GetComputerName() throws UnknownHostException {
		String computerName;
		computerName = InetAddress.getLocalHost().getHostName();		  
		return computerName;
	}
	
	public static String GetCurrentDir() {
		return new File(".").getAbsolutePath();
	}
}
