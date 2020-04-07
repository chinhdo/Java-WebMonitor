package chinhdo.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import chinhdo.util.ConfigManager;

/** Sends notifications using SMTP emails */
public class SmtpHelper {
	/** Constructor */
    public SmtpHelper(ConfigManager config) {
		this.config = config;
    }

	/** Sends anotification */
    public void send(String fromAddr, String toEmails, String subject, String body) {
		final String host = config.getString("smtp.host");
		final int port = config.getInt("smtp.port");
		final String login = config.getString("smtp.login");
		final String pwd = config.getString("smtp.pwd");
		// final String toEmail = config.getString("smtp.notifyEmails");
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", port); //SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, pwd);
			}
		};
		
		Session session = Session.getDefaultInstance(props, auth);
		
		try {
			MimeMessage msg = new MimeMessage(session);
			//set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(fromAddr);
			msg.setReplyTo(InternetAddress.parse(fromAddr, false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8", "html");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails, false));

			Transport.send(msg);
		  }
		  catch (Exception e) {
			throw new RuntimeException(e);
		  }
	}
	
	private ConfigManager config;
}