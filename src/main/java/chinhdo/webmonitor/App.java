package chinhdo.webmonitor;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import chinhdo.mail.SmtpHelper;
import chinhdo.util.ConfigManager;
import chinhdo.util.Helper;

/** Main App */
public class App {
	public static void main(final String[] args) throws UnknownHostException {
		long started = System.currentTimeMillis();
		log.info("====> Starting application (V1.1.0.21). Current dir: " + Helper.GetCurrentDir() + ".");
		
		ConfigManager config = new ConfigManager("config.xml");
		UrlHelper urlHelper = new UrlHelper(config);

		Monitor monitor = new Monitor(config, urlHelper);
		MonitorResult result = monitor.start();

		SmtpHelper smtp = new SmtpHelper(config);
		Notifier notifier = new Notifier(config, smtp);
		notifier.process(result);

		log.info("Exiting elapsed=" + (System.currentTimeMillis() - started));
	}

	private static Logger  log = Logger.getLogger("chinhdo.webmonitor");
}