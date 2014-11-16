package aaf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;

/**
 * Servlet implementation class UploadFilesServlet
 */
@WebServlet("/UploadFilesServlet")
public class UploadFilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SERVER_CREDS_KEY = "serverCredentials";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFilesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SERVER_CREDS_KEY);
		System.out.println("creds from upload file servlet: " + creds);
		
		
		
	}

}
