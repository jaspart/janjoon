package com.starit.janjoonweb.ui.mb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJTask;

public class MailingService {

	private String smtp_host;
	private String smtp_port;
	private String userName;
	private String password;
	private boolean error;
	static Logger logger = Logger.getLogger("MailingService");

	public MailingService() {

		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String path = servletContext.getRealPath("WEB-INF" + File.separator
				+ "classes")
				+ File.separator;
		System.out.println(path);
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(path + "email.properties"));
			smtp_host = properties.getProperty("email.host");
			smtp_port = properties.getProperty("email.port");
			userName = properties.getProperty("email.username");
			password = properties.getProperty("email.password");
			error = false;
			logger.info("Mailing Service Successfully initialised");
		} catch (IOException e) {
			error = true;
			logger.error("Error While initialising Mailing Service");
		}
	}

	public String getSmtp_host() {
		return smtp_host;
	}

	public void setSmtp_host(String smtp_host) {
		this.smtp_host = smtp_host;
	}

	public String getSmtp_port() {
		return smtp_port;
	}

	public void setSmtp_port(String smtp_port) {
		this.smtp_port = smtp_port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void sendMail(String mail_to,List<JJContact> contacts, JJTask task, String subject) {

		if (!error) {
			Properties props = new Properties();
			props.put("mail.smtp.host", smtp_host);
			props.put("mail.smtp.socketFactory.port", smtp_port);
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", smtp_port);

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});

			try {

				Message message = new MimeMessage(session);
				 message.setRecipients(Message.RecipientType.TO,
				 InternetAddress.parse(mail_to));
				for (JJContact c : contacts)
					if(!c.getEmail().equalsIgnoreCase(mail_to))
					message.addRecipients(Message.RecipientType.CC,
							InternetAddress.parse(c.getEmail()));
				message.setSubject("Task: " + task.getName() + " Done ");
				message.setText("Task: " + task.getName() + " Done " + "\n\n"
						+ subject);

				Transport.send(message);

				logger.info("Message Successfully send");

			} catch (MessagingException e) {
				logger.error(new RuntimeException(e).getMessage());
			}
		}
	}

}
