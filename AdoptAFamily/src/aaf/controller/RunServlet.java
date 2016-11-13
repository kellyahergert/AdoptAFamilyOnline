package aaf.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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

/**
 * Servlet implementation class RunServlet
 */
@WebServlet("/RunServlet")
public class RunServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String attachmentSuffix = "FamilyWishList.pdf";
//	// Un-comment this variable for testing. 20 second sleep caused 2014 test data to take 4hr 15min to run
//	private static final long sleepTimeWithoutEmails = 1000 * 10; // sleep 10 seconds between all emails
       
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
		System.out.println(Calendar.getInstance().getTime());
		String totalSuccess = "";
		
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
		String storeDir = (String) request.getSession().getAttribute("storeDir");
		
	    LinkedList<Sponsor> sponsors = (LinkedList<Sponsor>) request.getSession().getAttribute("sponsors");

//	    PriorityQueue<Family> families = (PriorityQueue<Family>) request.getSession().getAttribute("families");
	    
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		
		//TODO don't hardcode sender address
		BasicEmailSender emailSender = new BasicEmailSender("JLittlejohn@DenRescue.org", creds);
		
		boolean actuallySendEmails = (request.getParameter("sendEmailsCheckbox") != null);
		System.out.println("actuallySendEmails: " + actuallySendEmails);

		FileWriter sponsorWriter = new FileWriter(storeDir + "/SponsorEmails.doc");
		
		List<Family> adoptedFamilies;

		request.getSession().setMaxInactiveInterval(8*60*60); // 8 hour session timeout, cause it's sad when this times out
		
		PriorityQueue<Family> unmatchedFamilies =
				(PriorityQueue<Family>) request.getSession().getAttribute("unmatchedFamilies");

		int totalNumSponsors = 0;
		if (sponsors != null)
		{
			totalNumSponsors = sponsors.size();
		}
		
		int totalWaitlistedFamilies = 0;
		if (unmatchedFamilies != null)
		{
			totalWaitlistedFamilies = unmatchedFamilies.size();
		}
		
		int sponsorCounter = 0;
		int waitlistCounter = 0;
		String progressMessage = "";
		
		for(Sponsor sponsor : sponsors){
			++sponsorCounter;
			adoptedFamilies = sponsor.getAdoptedFams();
			
			if (!adoptedFamilies.isEmpty()){
				System.out.println("Sending email to Spon ID " + sponsor.getSponId() + " at " + sponsor.getEmailAddress());
	
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
					for (Family family : adoptedFamilies){
					    MimeBodyPart famBodyPart = new MimeBodyPart();
					    DataSource famSource = new FileDataSource(family.getAttachmentName());
					    famBodyPart.setDataHandler(new DataHandler(famSource));
					    famBodyPart.setFileName(family.getLastName() + attachmentSuffix);
					    
					    attachments.add(famBodyPart);
					}
				    
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
				sponsorWriter.writeToFile("\n\nTO:" + sponsor.getEmailAddress() + "\n" + sponsorEmailText + "\n========================================\n");
				
				// Sponsor emails
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
	//			// un-comment this code block for testing
	//			else if (sleepTimeWithoutEmails > 0)
	//			{
	//				System.out.println("Waiting " + sleepTimeWithoutEmails/1000 + " seconds instead of sending email to sponsor " + sponsor.getSponId());
	//				try {
	//					Thread.sleep(sleepTimeWithoutEmails);
	//				} catch (InterruptedException e) {
	//					e.printStackTrace();
	//				}
	//				System.out.println(Calendar.getInstance().getTime());
	//			}
				
				// let nominator of each adopted family know their family was adopted
				FileWriter nominatorWriter = new FileWriter(storeDir + "/NominatorEmails.doc");
				for (Family family : adoptedFamilies){
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
	//				// un-comment this code block for testing
	//				else if (sleepTimeWithoutEmails > 0)
	//				{
	//					System.out.println("Waiting " + sleepTimeWithoutEmails/1000 + " seconds instead of sending email to nominator of family " + family.getId());
	//					try {
	//						Thread.sleep(sleepTimeWithoutEmails);
	//					} catch (InterruptedException e) {
	//						e.printStackTrace();
	//					}
	//					System.out.println(Calendar.getInstance().getTime());
	//				}
					
				}
				nominatorWriter.close();

				progressMessage = ">>> Emails sent: [" + Integer.toString(sponsorCounter) + " of " + Integer.toString(totalNumSponsors) + " Sponsors]";
				progressMessage += " and [" + Integer.toString(waitlistCounter) + " of " + Integer.toString(totalWaitlistedFamilies) + " wait listed families] <<<";
				System.out.println(progressMessage);
			}else{
				System.out.println(">>>>>> No email needed for [" + Integer.toString(sponsorCounter) + " of " + Integer.toString(totalNumSponsors) + " Sponsors]");
			}

		}
		
		sponsorWriter.close();
		
		PriorityQueue<SponsorEntry> sponsorEntries =
				(PriorityQueue<SponsorEntry>) request.getSession().getAttribute("unmatchedSponsors");
		

		if (unmatchedFamilies != null || 
			(sponsorEntries != null && !sponsorEntries.isEmpty()))
		{
			
			FileWriter unmatchedWriter = new FileWriter(storeDir + "/unmatchedPeople.doc");
			
			if (unmatchedFamilies != null)
			{
				// write lists of unmatched peeps to a file
				
				FileWriter waitlistWriter = new FileWriter(storeDir + "/WaitlistEmails.doc");
				
				unmatchedWriter.writeToFile("\n===Unmatched Families===\n");
				
				for (Family fam : unmatchedFamilies)
				{
					System.out.println("emailing waitlisted Family ID " + fam.getId());
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
		//			// un-comment this code block for testing
		//			else if (sleepTimeWithoutEmails > 0)
		//			{
		//				System.out.println("Waiting " + sleepTimeWithoutEmails/1000 + " seconds instead of sending waitlist email to nominator of family " + fam.getId());
		//				try {
		//					Thread.sleep(sleepTimeWithoutEmails);
		//				} catch (InterruptedException e) {
		//					e.printStackTrace();
		//				}
		//			}
					++waitlistCounter;
					progressMessage = ">>> Emails sent: [" + Integer.toString(sponsorCounter) + " of " + Integer.toString(totalNumSponsors) + " Sponsors]";
					progressMessage += " and [" + Integer.toString(waitlistCounter) + " of " + Integer.toString(totalWaitlistedFamilies) + " wait listed families] <<<";
					System.out.println(progressMessage);
				}
				
				waitlistWriter.close();
			}
		
			if (sponsorEntries != null && !sponsorEntries.isEmpty())
			{
				unmatchedWriter.writeToFile("\n===Unmatched Sponsor Entries===\n");
				
				for (SponsorEntry spon : sponsorEntries)
				{
					unmatchedWriter.writeToFile("\n" + spon.toString());
				}
				
			}

			unmatchedWriter.close();
	    }
		
		System.out.println("Done!");
	}
	

}
