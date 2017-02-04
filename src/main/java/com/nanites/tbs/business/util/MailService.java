package com.nanites.alphaplus.business.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
	private String defaultFrom;
	private static String userName;
	private static String password;
	Properties properties;
	Session session;

	public MailService() {
	}

	public MailService(String mail_smtp_host, String mail_smtp_port, String mail_transport_protocol,
			String mail_smtp_auth, String mail_smtp_starttls_enable, String mail_smtp_socketFactory_port,
			String mail_smtp_socketFactory_class, String defaultFrom, String userName, String password) {
		MailService.userName = userName;
		MailService.password = password;

		this.defaultFrom = defaultFrom;

		properties = new Properties();
		properties.setProperty("mail.smtp.host", mail_smtp_host);
		properties.setProperty("mail.smtp.port", mail_smtp_port);
		properties.setProperty("mail.transport.protocol", mail_transport_protocol);
		properties.setProperty("mail.smtp.auth", mail_smtp_auth);
		properties.setProperty("mail.smtp.starttls.enable", mail_smtp_starttls_enable);
		properties.setProperty("mail.smtp.socketFactory.port", mail_smtp_socketFactory_port);
		properties.setProperty("mail.smtp.socketFactory.class", mail_smtp_socketFactory_class);

		session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MailService.userName, MailService.password);
			}
		});
	}

	private static MimeMessage createMessage(Session session, String subject, String from, String textMessage)
			throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setSubject(subject);
		message.setText(textMessage);
		message.setSentDate(new java.util.Date());
		return message;
	}

	public void sendMail(String subject, String textMessage, String from, List<String> to)
			throws AddressException, MessagingException {
		MimeMessage message = MailService.createMessage(this.session, subject, (from == null) ? this.defaultFrom : from,
				textMessage);
		for (int i = 0; i < to.size(); i++) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(i)));
		}
		Transport.send(message);
	}

	public void sendMail(String subject, String textMessage, String from, String to)
			throws AddressException, MessagingException {
		MimeMessage message = MailService.createMessage(this.session, subject, (from == null) ? this.defaultFrom : from,
				textMessage);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		Transport.send(message);
	}

	public static void main(String[] args) throws AddressException, MessagingException {
		String to = "hdk.pnchl@gmail.com";
		String textMessage = "textMessage textMessage textMessage textMessage textMessage";
		String subject = "HELP from DR-AAKASHEE-SHAH COM";
		String from = "help@dr-aakashee-shah.com";
		String mail_transport_protocol = "smtp";
		String mail_smtp_host = "smtp.office365.com";
		String mail_smtp_port = "587";
		String mail_smtp_auth = "true";
		String mail_smtp_starttls_enable = "true";
		String mail_smtp_socketFactory_port = "465";
		String mail_smtp_socketFactory_class = "javax.net.ssl.SSLSocketFactory";
		String user = "help@dr-aakashee-shah.com";
		String password = "iAmA$$a89";

		MailService mailService = new MailService(mail_smtp_host, mail_smtp_port, mail_transport_protocol,
				mail_smtp_auth, mail_smtp_starttls_enable, mail_smtp_socketFactory_port, mail_smtp_socketFactory_class,
				from, user, password);
		mailService.sendMail(subject, textMessage, mailService.defaultFrom, to);
	}
}

// http://www.javaquery.com/2011/09/how-to-send-mail-from-godaddy-java-mail.html