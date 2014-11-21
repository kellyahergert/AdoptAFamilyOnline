package aaf.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;
import aaf.model.Family;
import aaf.model.FileWriter;
import aaf.model.Person;
import aaf.model.Sponsor;
import aaf.model.SponsorEntry;
import aaf.model.SponsorEntry.FamilyType;

/**
 * Servlet implementation class RunServlet
 */
@WebServlet("/RunServlet")
public class RunServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String attachmentSuffix = "FamilyWishList.pdf";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RunServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RUNNING!!!!!!!!!");
		
		String totalSuccess = "";
		
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
		String storeDir = (String) request.getSession().getAttribute("storeDir");
		
	    LinkedList<Sponsor> sponsors = (LinkedList<Sponsor>) request.getSession().getAttribute("sponsors");

//	    PriorityQueue<Family> families = (PriorityQueue<Family>) request.getSession().getAttribute("families");
	    
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		BasicEmailSender emailSender = new BasicEmailSender("JLittlejohn@DenRescue.org", creds);
		
		boolean actuallySendEmails = (request.getParameter("sendEmailsCheckbox") != null);
		System.out.println("actuallySendEmails: " + actuallySendEmails);

		FileWriter sponsorWriter = new FileWriter(storeDir + "/SponsorEmails.doc");
		
		HashMap<Family, FamilyType> adoptedFamilies;

//		while (!sponsors.isEmpty())
//		{
//			Sponsor deleteMe = sponsors.poll();
//			System.out.println("not emailing " + deleteMe.getSponId());
//			
//			if (deleteMe.getSponId() == 244)
//			{
//				break;
//			}
//		}

		for(Sponsor sponsor : sponsors){
			System.out.println("Sending email to " + sponsor.getSponId());
			
			adoptedFamilies = sponsor.getAdoptedFams();

			String sponsorEmailText = (String) request.getSession().getAttribute("sponsorEmailText");
			sponsorEmailText = EmailConverter.convertSponsorEmailText(sponsorEmailText, sponsor);
			
			LinkedList<MimeBodyPart> attachments = new LinkedList<MimeBodyPart>();
			
		    try {
		    	// FAQ pdf attachment
			    MimeBodyPart faqBodyPart = new MimeBodyPart();
			    DataSource faqSource = new FileDataSource((String)request.getSession().getAttribute("faqLoc"));
				faqBodyPart.setDataHandler(new DataHandler(faqSource));
			    faqBodyPart.setFileName("AAF_FAQ.pdf");
			    
			    attachments.add(faqBodyPart);
			    
		    	// sponsor obligations pdf attachment
			    MimeBodyPart sponObligationsBodyPart = new MimeBodyPart();
			    DataSource sponObligationsSource = new FileDataSource((String)request.getSession().getAttribute("sponObligationLoc"));
			    sponObligationsBodyPart.setDataHandler(new DataHandler(sponObligationsSource));
			    sponObligationsBodyPart.setFileName("Sponsor_Obligations.pdf");
			    
			    attachments.add(sponObligationsBodyPart);
			    
			    // family wishlist attachments
				for (Family family : adoptedFamilies.keySet()){
				    MimeBodyPart famBodyPart = new MimeBodyPart();
				    DataSource famSource = new FileDataSource(family.getAttachmentName());
				    famBodyPart.setDataHandler(new DataHandler(famSource));
				    famBodyPart.setFileName(family.getLastName() + attachmentSuffix);
				    
				    attachments.add(famBodyPart);
				}
			    
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sponsorWriter.writeToFile("\n\nTO:" + sponsor.getEmailAddress() + "\n" + sponsorEmailText + "\n========================================\n");
			
			if (actuallySendEmails)
			{
				String sponSuccess = emailSender.sendEmail("Adopt A Family Information", sponsorEmailText, sponsor.getEmailAddress(), attachments);
			
				totalSuccess += "\n" + sponsor.getEmailAddress() + " : " + sponSuccess;
				request.getSession().setAttribute("statusMsg", totalSuccess);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// let nominator of each adopted family know their family was adopted
			FileWriter nominatorWriter = new FileWriter(storeDir + "/NominatorEmails.doc");
			for (Family family : adoptedFamilies.keySet()){
				Person nominator = family.getNominator();
				
				String nominatorEmailText = (String) request.getSession().getAttribute("nominatorEmailText");
				nominatorEmailText = EmailConverter.convertNominatorEmailText(nominatorEmailText, family);
				
				nominatorWriter.writeToFile("\n\nTO:" + nominator.getEmailAddress() + "\n" + nominatorEmailText + "\n========================================\n");
				
				if (actuallySendEmails)
				{
					String nomSuccess = emailSender.sendEmail("Adopt A Family Information", nominatorEmailText, nominator.getEmailAddress());
					
					totalSuccess += "\n" + nominator.getEmailAddress() + " : " + nomSuccess;
					request.getSession().setAttribute("statusMsg", totalSuccess);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			nominatorWriter.close();
			
		}
		
		sponsorWriter.close();
		
		PriorityQueue<Family> unmatchedFamilies =
				(PriorityQueue<Family>) request.getSession().getAttribute("unmatchedFamilies");
		PriorityQueue<SponsorEntry> sponsorEntries =
				(PriorityQueue<SponsorEntry>) request.getSession().getAttribute("unmatchedSponsors");
		

		
		// write lists of unmatched peeps to a file
		
		FileWriter unmatchedWriter = new FileWriter(storeDir + "/unmatchedPeople.doc");
		FileWriter waitlistWriter = new FileWriter(storeDir + "/WaitlistEmails.doc");
		
//		while (!unmatchedFamilies.isEmpty())
//		{
//			Family fam = unmatchedFamilies.poll();
//			System.out.println("not emailing " + fam.getId());
//			if (fam.getId() == 294)
//			{
//				break;
//			}
//		}
		
		unmatchedWriter.writeToFile("\n===Unmatched Families===\n");
		
		for (Family fam : unmatchedFamilies)
		{
			System.out.println("emailing " + fam.getId());
			String famWaitlistEmailText = (String) request.getSession().getAttribute("nominatorWaitListedEmailText");
			
			famWaitlistEmailText = EmailConverter.convertNominatorEmailText(famWaitlistEmailText, fam);
			
			waitlistWriter.writeToFile("\n\nTO:" + fam.getNominator().getEmailAddress() + "\n" + famWaitlistEmailText + "\n========================================\n");

			unmatchedWriter.writeToFile("\n" + fam.toString());
			
			if (actuallySendEmails)
			{
				String unmatchedFamSuccess = emailSender.sendEmail("Adopt A Family WaitList", famWaitlistEmailText, fam.getNominator().getEmailAddress());
				
				totalSuccess += "\n" + fam.getNominator().getEmailAddress() + " : " + unmatchedFamSuccess;
				
				request.getSession().setAttribute("statusMsg", totalSuccess);
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}


		}
		
		waitlistWriter.close();
		
		unmatchedWriter.writeToFile("\n===Unmatched Sponsor Entries===\n");
		
		for (SponsorEntry spon : sponsorEntries)
		{
			unmatchedWriter.writeToFile("\n" + spon.toString());
		}
		
		unmatchedWriter.close();
		
		System.out.println("Done!");
	}
	

}
