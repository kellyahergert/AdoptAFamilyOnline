package aaf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;
import aaf.model.FileLocations;

/**
 * Servlet implementation class UploadFilesServlet
 */
@WebServlet("/UploadFilesServlet")
public class UploadFilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		System.out.println("server creds key " + SessionAttributeConstants.SERVER_CREDS_KEY);
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		System.out.println("creds from upload file servlet: " + creds);
		
		FileLocations fileLocs = new FileLocations(
				request.getParameter("sponsorEmailTextFile"),
				request.getParameter("familyEmailTextFile"),
				request.getParameter("familyCSV"),
				request.getParameter("sponsorCSV"),
				request.getParameter("faqPdf"),
				request.getParameter("familyPdfs"));
		
		request.getSession().setAttribute(SessionAttributeConstants.FILE_LOCS_KEY, fileLocs);
		
		request.getRequestDispatcher("aaf3_family_matching.html").forward(request, response);
		
	}

}
