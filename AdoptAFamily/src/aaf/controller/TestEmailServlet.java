package aaf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;
import aaf.model.Family;
import aaf.model.Nominator;
import aaf.model.Sponsor;

/**
 * Servlet implementation class TestEmailServlet
 */
@WebServlet("/TestEmailServlet")
public class TestEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestEmailServlet() {

    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		BasicEmailSender emailSender = new BasicEmailSender("someEmailAddress@gmail.com", creds);
		
		if (request.getParameter("sendTestSponsorEmail") != null)
		{
			String sponsorEmailText = (String) request.getSession().getAttribute("sponsorEmailText");
			Sponsor sponsor = new Sponsor(1, "John", "Johnson", request.getParameter("testEmail"), 1, 2, 3);
			sponsorEmailText = EmailConverter.convertSponsorEmailText(sponsorEmailText, sponsor);

			emailSender.sendEmail("Adopt A Family Information", sponsorEmailText, sponsor.getEmailAddress()); 
		}
		else if (request.getParameter("sendTestNominatorEmail") != null)
		{
			String nominatorEmailText = (String) request.getSession().getAttribute("nominatorEmailText");
			Nominator nominator = new Nominator("Brooke", "Nominator", request.getParameter("testEmail"));
			Family family = new Family(1, nominator, "Sherlock", "Katie", "SSherlock",
					"family@fam.com", 5, "test");
			System.out.println(nominatorEmailText);
			System.out.println(family);
			nominatorEmailText = EmailConverter.convertNominatorEmailText(nominatorEmailText, family);
			
			emailSender.sendEmail("Adopt A Family Information", nominatorEmailText, family.getNominator().getEmailAddress()); 
		}
		else if (request.getParameter("sendTestWaitListEmail") != null)
		{
			String waitListedEmailText = (String) request.getSession().getAttribute("nominatorWaitListedEmailText");
			Nominator nominator = new Nominator("Brooke", "Nominator", request.getParameter("testEmail"));
			Family family = new Family(1, nominator, "Sherlock", "Katie", "SSherlock",
					"family@fam.com", 5, "test");
			System.out.println(waitListedEmailText);
			System.out.println(family);
			waitListedEmailText = EmailConverter.convertNominatorEmailText(waitListedEmailText, family);
			
			emailSender.sendEmail("Adopt A Family Information", waitListedEmailText, family.getNominator().getEmailAddress()); 
		}
		
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
	}

}