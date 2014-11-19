package aaf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;
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
		
		if (request.getParameter("sendTestSponsorEmail") != null)
		{
			String sponsorEmailText = (String) request.getSession().getAttribute("sponsorEmailText");
			Sponsor sponsor = new Sponsor(1, "John", "Johnson", request.getParameter("testEmail"), 1, 2, 3);
			EmailConverter.convertSponsorEmailText(sponsorEmailText, sponsor);
			EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
			System.out.println("run servlet " + creds);
			BasicEmailSender emailSender = new BasicEmailSender("someEmailAddress@gmail.com", creds);
			
			emailSender.sendEmail("Adopt A Family Information", sponsorEmailText, sponsor.getEmailAddress()); 
		}
		else if (request.getParameter("sendTestNominatorEmail") != null)
		{
			
		}
		
	}

}
