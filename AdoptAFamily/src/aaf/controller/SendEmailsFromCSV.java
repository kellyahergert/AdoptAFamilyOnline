package aaf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;

import aaf.model.EmailServerCredentials;
import aaf.model.Family;
import aaf.model.FamilyReader;

public class SendEmailsFromCSV {

	public static void main(String[] args) {

		SendEmailsFromCSV emailSender = new SendEmailsFromCSV();
		
		System.out.println("Enter email server password: ");
		Scanner in = new Scanner(System.in);
		String password = in.nextLine();
		in.close();
		
		emailSender.sendWaitlistEmails("C:/aaf_test/4_2014familylist.csv", "C:/aaf_test/wishlists", password, true);

	}
	
	public void sendWaitlistEmails(String file, String storeDir, String password, boolean actuallySendEmails)
	{
		try {
			EmailServerCredentials creds = new EmailServerCredentials("smtp.comcast.net", 587, "jordhergert@comcast.net", password);
//			EmailServerCredentials creds = new EmailServerCredentials("1200exchange001.drm.local", 587, "DRM/jhergert", password);
			
			BasicEmailSender emailSender = new BasicEmailSender("JLittlejohn@DenRescue.org", creds);
			
		    Scanner nominatorWaitListEmail = new Scanner(new File("C:/aaf_test/3_WaitListEmailHTML.txt"));
		    String nominatorWaitListEmailText = "";
		    
		    while (nominatorWaitListEmail.hasNextLine())
		    {
		    	nominatorWaitListEmailText += nominatorWaitListEmail.nextLine();
		    }
		    
		    nominatorWaitListEmail.close();
			
			InputStream input = new FileInputStream(new File(file));
			FamilyReader famReader = new FamilyReader(input, storeDir);
			
			LinkedList<Family> families = new LinkedList<Family>();
			famReader.createFamilyObjects(families);
			
			String emailText = "";
			
			for (Family fam : families)
			{
				System.out.println("\nSending email to family id " + fam.getId() + " nominator " + fam.getNominator().getFirstName() + " " + fam.getNominator().getLastName() +
						" email=" + fam.getNominator().getEmailAddress());
				
				if (actuallySendEmails)
				{
					emailText = EmailConverter.convertNominatorEmailText(nominatorWaitListEmailText, fam);
					boolean unmatchedFamSuccess = false;
					long waitTime = 100;
					
					while (!unmatchedFamSuccess)
					{
						System.out.println("*trying " + waitTime + " milliseconds*");
						
						unmatchedFamSuccess = emailSender.sendEmailBool("Adopt A Family WaitList", emailText, fam.getNominator().getEmailAddress());
						
						try 
						{
							Thread.sleep(waitTime);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						
						if (waitTime < 204800)
						{
							waitTime *= 2;
						}
						else
						{
							waitTime = 100;
						}
					}
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

}
