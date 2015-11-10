package aaf.controller;


import java.util.LinkedList;
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

	/**
	 * Main method is for testing purposes only
	 * 
	 * @param args
	 * 			not used
	 */
	public static void main(String[] args)
	{
		String fromAddress = "kellytorkelson@gmail.com";
		EmailServerCredentials creds = new EmailServerCredentials("smtp.comcast.net", 587, "jordhergert@comcast.net", "comcastPasswordGoesHere");

		BasicEmailSender sender = new BasicEmailSender(fromAddress, creds);
		for (int i=1; i <= 5; i++)
		{
			System.out.println("====== Email " + i + " ============");
			sender.sendEmail("subject " + i, "kitten email text " + i, "kellyahergert@gmail.com");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.starttls.enable", "true");

			Authenticator auth = new Authenticator()
			{
                protected PasswordAuthentication getPasswordAuthenticator()
                {
                        return new PasswordAuthentication(username, password);
                }
			};

			mySession = Session.getInstance(properties, auth);

		}
		else
		{
			System.out.println("sending unauthenticated");
			mySession = Session.getDefaultInstance(properties);
		}
	}
	
	public String sendEmail(String theSubject, String theText, String theToAddress)
	{
		boolean success = sendEmailBool(theSubject, theText, theToAddress);
		if (success)
		{
			return "Email sent successfully!";
		}
		else
		{
			return "Failed to send email";
		}
	}
	
	public boolean sendEmailBool(String theSubject, String theText, String theToAddress)
	{
		return sendEmailBool(theSubject, theText, theToAddress, new LinkedList<MimeBodyPart>());
	}
	
	public String sendEmail(String theSubject, String theText, String theToAddress, LinkedList<MimeBodyPart> attachments)
	{
		boolean success = sendEmailBool(theSubject, theText, theToAddress, attachments);
		if (success)
		{
			return "Email sent successfully!";
		}
		else
		{
			return "Failed to send email";
		}
	}
	
	public boolean sendEmailBool(String theSubject, String theText, String theToAddress, LinkedList<MimeBodyPart> attachments)
	{
		long waitTime = 100;

		final int FIVE_MINUTES = 300000;
		while (waitTime <= FIVE_MINUTES) // break from loop on success or wait time of 5 minutes
		{
			try
			{
				System.out.println("Attempt sending message to " + theToAddress + "....");
				
				MimeMessage message = new MimeMessage(mySession);

				message.setFrom(new InternetAddress(myFromAddress));

				message.addRecipient(Message.RecipientType.TO, new InternetAddress(
						theToAddress));

				message.setSubject(theSubject);

				MimeBodyPart messageBodyPart = new MimeBodyPart();

				messageBodyPart.setContent(theText, "text/html; charset=utf-8");

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				
				for (MimeBodyPart attachment : attachments)
				{
					multipart.addBodyPart(attachment);
				}
				
				message.setContent(multipart);

				if (myServerCredentials.getUsername() != "" && myServerCredentials.getPassword() != "")
				{
					// uncomment this block to inject exceptions for testing purposes
//					int rand = (int) (Math.random() * 10);
//					if (rand % 2 == 0)
//					{
//						throw new MessagingException("Test Msg Exception");
//					}

					Transport transport = mySession.getTransport();
					
					transport.connect(myServerCredentials.getHost(),
					  myServerCredentials.getPort(),
					  myServerCredentials.getUsername(),
					  myServerCredentials.getPassword());
					
					// send the email
					transport.sendMessage(message, message.getAllRecipients());
				}
				else
				{
					Transport.send(message);
				}
				
				System.out.println("---Sent message successfully to " + theToAddress + "....");
				return true;
			}
			catch (MessagingException mex)
			{			
				System.out.println(mex.getMessage());
			}
			
			// exponential back-off if email wasn't sent successfully
			try 
			{
				Thread.sleep(waitTime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			waitTime *= 2;
		}
		return false;
	}
	
}
