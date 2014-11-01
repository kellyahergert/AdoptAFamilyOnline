package aaf.controller;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import aaf.model.EmailServerCredentials;

public class BasicEmailSender {

	protected String myFromAddress = "";
	protected EmailServerCredentials myServerCredentials;
	protected Session mySession;

	public static void main(String[] args)
	{
		String fromAddress = "kellytorkelson@gmail.com";
		EmailServerCredentials creds = new EmailServerCredentials("smtp.comcast.net", "jordhergert@comcast.net", "Weasel6^");
		
		BasicEmailSender sender = new BasicEmailSender(fromAddress, creds);
		sender.sendEmail("subject", "email text", "kellyahergert@gmail.com");
	}
	
	public BasicEmailSender(String fromAddress, EmailServerCredentials serverCredentials) {
		this.myFromAddress = fromAddress;
		this.myServerCredentials = serverCredentials;
		
		final String username = serverCredentials.getUsername();
		final String password = serverCredentials.getPassword();
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", serverCredentials.getHost());
		
		if (username != "" && password != "")
		{
			properties.setProperty("mail.smtp.port", "587");
			properties.setProperty("mail.smtp.auth", "true");
//			properties.setProperty("mail.smtp.starttls.enable", "true");
			
			Authenticator auth = new Authenticator(){
                protected PasswordAuthentication getPasswordAuthenticator(){
                        return new PasswordAuthentication(username, password);
                }
			};

			mySession = Session.getInstance(properties, auth);
		}
		else
		{

//			properties.setProperty("mail.smtp.auth", "false");
			System.out.println("sending unauthenticated");
			mySession = Session.getDefaultInstance(properties);
		}
	}
	
	public String sendEmail(String theSubject, String theText, String theToAddress)
	{
		try {
			MimeMessage message = new MimeMessage(mySession);

			message.setFrom(new InternetAddress(myFromAddress));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					theToAddress));

			message.setSubject(theSubject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(theText, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			
			if (myServerCredentials.getUsername() != "" && myServerCredentials.getPassword() != "")
			{
				Transport transport = mySession.getTransport();
				transport.connect("smtp.comcast.net", 587, myServerCredentials.getUsername(), myServerCredentials.getPassword());
				transport.sendMessage(message, message.getAllRecipients());
			}
			else
			{
				Transport.send(message);
			}
			
			System.out.println("Sent message successfully to " + theToAddress + "....");
			return "Email was successfully sent to " + theToAddress;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return "Failed to send email to " + theToAddress;
		}
	}
	
}
