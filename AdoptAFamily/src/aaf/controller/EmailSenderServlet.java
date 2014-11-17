package aaf.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;

/**
 * Servlet implementation class EmailSenderServlet
 */
@WebServlet("/EmailSenderServlet")
public class EmailSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmailSenderServlet() {
        super();
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Enumeration<String> params = request.getParameterNames();
//		
//		while (params.hasMoreElements())
//		{
//			String param = params.nextElement();
//			
//			System.out.println(param + " : " + request.getParameter(param));
//		}

		EmailServerCredentials creds = new EmailServerCredentials(
				request.getParameter("smtpServerIP"),
				Integer.parseInt(request.getParameter("smtpServerPort")), // TODO handle NumberFormatException?
				request.getParameter("smtpServerLogin"),
				request.getParameter("smtpServerPassword"));

		if (request.getParameter("sendTestSponsorEmail") != null)
		{
			BasicEmailSender sender = new BasicEmailSender(request.getParameter("testEmailFromAddr"), creds);
			String emailStatus = sender.sendEmail("subject", "sent from my servlet!", request.getParameter("testEmailToAddr"));
			
			request.setAttribute("emailResponseMsg", emailStatus);
	//		request.setAttribute("emailInputValue", "emailinputfieldvalue");  // add value="$emailInputValue" to jsp input tag
	
			request.getRequestDispatcher("aaf1_email_info.jsp").forward(request, response);
		}
		else if (request.getParameter("goToAaf2") != null)
		{
			
			request.getSession().setAttribute(SessionAttributeConstants.SERVER_CREDS_KEY, creds);
			request.getRequestDispatcher("aaf2_files.html").forward(request, response);
			

			/*
	        // Obtain a database connection:
	        EntityManagerFactory emf =
	           (EntityManagerFactory)getServletContext().getAttribute("emf");
	        EntityManager em = emf.createEntityManager();
	 
	        try {

//	            // see if user is already in the database
//	            List<EmailServerCredentials> userCredsList = em.createQuery(
//		                "SELECT cred FROM EmailServerCredentials cred WHERE username='" + request.getParameter("smtpServerLogin") + "'",
//		                EmailServerCredentials.class).getResultList();
	            
	            List<EmailServerCredentials> credsList = em.createQuery(
		                "SELECT cred FROM EmailServerCredentials cred",
		                EmailServerCredentials.class).getResultList();
		            
	            boolean userFound = false;
	            for (EmailServerCredentials cred : credsList)
	            {
	            	if (cred.getUsername().equals(request.getParameter("smtpServerLogin")))
	            	{
	            		userFound = true;
	            	}
	            }
	            
//	            if (userCredsList.isEmpty())
		        if (!userFound)
	            {
	            	System.out.println("Adding user to db " + request.getParameter("smtpServerLogin"));
	                em.getTransaction().begin();
		            // persist the email server credentials for future email sending
	                em.persist(creds);
	                em.getTransaction().commit();
	            }

	            List<EmailServerCredentials> credsList2 = em.createQuery(
	                "SELECT cred FROM EmailServerCredentials cred",
	                EmailServerCredentials.class).getResultList();
	            
	            for (EmailServerCredentials cred : credsList2)
	            {
	            	System.out.println("cred from db " + cred);
	            }

	 
	        } finally {
	            // Close the database connection:
	            if (em.getTransaction().isActive())
	                em.getTransaction().rollback();
	            em.close();
	        }
	        */
			

		}
		else
		{
			System.out.println("unknown post request");
		}
	}

}
