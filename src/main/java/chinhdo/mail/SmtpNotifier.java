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

public class SmtpNotifier {
    private String smtpHost;
    private Integer smtpPort;
    private String smtpLogin;
    private String smtpPassword;
    private String notifyEmails;

    public SmtpNotifier(String host, int port, String login,
        String pwd, String notifyEmails) {
        this.smtpHost = host;
        this.smtpPort = port;
        this.smtpLogin = login;
        this.smtpPassword = pwd;
        this.notifyEmails = notifyEmails;
    }

    public void notify(String subject, String body) {
		final String toEmail = this.notifyEmails;
		
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtpHost);
		props.put("mail.smtp.socketFactory.port", this.smtpPort.toString()); //SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.smtpPort);
        
        final String login = this.smtpLogin;
        final String password = this.smtpPassword;
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, password);
			}
		};
		
		Session session = Session.getDefaultInstance(props, auth);
        sendEmail(session, toEmail, subject, body);
        // EmailUtil.sendAttachmentEmail(session, toEmail,"SSLEmail Testing Subject with Attachment", "SSLEmail Testing Body with Attachment");
        // EmailUtil.sendImageEmail(session, toEmail,"SSLEmail Testing Subject with Image", "SSLEmail Testing Body with Image");
    }

    /**
	 * Utility method to send simple HTML email
	 */
	private void sendEmail(Session session, String toEmail, String subject, String body){
        try {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("no_reply@chinhdo.com", "Java Web Monitor"));
	      msg.setReplyTo(InternetAddress.parse("no_reply@chinhdo.com", false));
	      msg.setSubject(subject, "UTF-8");
	      msg.setText(body, "UTF-8");
	      msg.setSentDate(new Date());
	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
    	  Transport.send(msg);  
	    }
	    catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	}
}