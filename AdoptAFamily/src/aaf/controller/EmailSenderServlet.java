package aaf.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		
		int smtpServerPort = 0;
		try
		{
			smtpServerPort = Integer.parseInt(request.getParameter("smtpServerPort"));
		}
		catch (NumberFormatException e)
		{
			System.out.println("Must supply an integer for the Mail Server Port");
			request.getRequestDispatcher("aaf1_email_info.jsp").forward(request, response);
			return;
		}

		EmailServerCredentials creds = new EmailServerCredentials(
				request.getParameter("smtpServerIP"),
                smtpServerPort,
				request.getParameter("smtpServerLogin"),
				request.getParameter("smtpServerPassword"));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();

		File storeDir = new File("C:/AAF/" + dateFormat.format(date) + "/");
		storeDir.mkdirs();
		
		request.getSession().setAttribute("storeDir", storeDir.toPath().toString());
		request.getSession().setAttribute(SessionAttributeConstants.SERVER_CREDS_KEY, creds);

		if (request.getParameter("sendTestSponsorEmail") != null)
		{
			BasicEmailSender sender = new BasicEmailSender(request.getParameter("testEmailFromAddr"), creds);
			String emailStatus = sender.sendEmail("subject", "sent from my servlet!", request.getParameter("testEmailToAddr"));
			
			request.setAttribute("emailResponseMsg", emailStatus);
			request.setAttribute("smtpServerIP", creds.getHost());
			request.setAttribute("smtpServerPort", creds.getPort());
			request.setAttribute("smtpServerLogin", creds.getUsername());
			request.setAttribute("smtpServerPassword", creds.getPassword());
			request.setAttribute("smtpServerLogin", creds.getUsername());
			
			request.getRequestDispatcher("aaf1_email_info.jsp").forward(request, response);
		}
		else if (request.getParameter("goToAaf2") != null)
		{
			
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
		else if (request.getParameter("goToAafDbFiles") != null)
		{
			request.getRequestDispatcher("aaf2.1_db_files.html").forward(request, response);
		}
		else
		{
			System.out.println("unknown post request");
		}
	}

}
