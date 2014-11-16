package aaf.controller;

import java.io.IOException;
import java.util.Enumeration;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("EmaiSenderServlet.doGet called!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("EmaiSenderServlet.doPost called!");
        Enumeration<String> params = request.getParameterNames();
		
//		while (params.hasMoreElements())
//		{
//			String param = params.nextElement();
//			
//			System.out.println(param + " : " + request.getParameter(param));
//		}

		EmailServerCredentials creds = new EmailServerCredentials(
				request.getParameter("smtpServerIP"),
				request.getParameter("emailLogin"),
				request.getParameter("emailPassword"));

		BasicEmailSender sender = new BasicEmailSender(request.getParameter("emailInput"), creds);
		sender.sendEmail("subject", "sent from my servlet!", request.getParameter("testEmail"));
		
		request.setAttribute("emailResponseMsg", "Email Sent Successfully!");
		request.setAttribute("emailInput", "myliltest");
		request.getRequestDispatcher("aaf1_email_info.jsp").forward(request, response);
		
	}

}
