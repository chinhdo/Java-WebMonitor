package chinhdo.util;
import java.util.*;
import java.io.File;
import java.lang.*;
import java.net.*;

public class Helper {

	public static String GetComputerName() {
		String computerName;
		try {
			computerName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		  
		return computerName;
	}
	
	public static String GetCurrentDir() {
		return new File(".").getAbsolutePath();
	}
}
