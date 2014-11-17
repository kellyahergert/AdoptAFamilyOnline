package aaf.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import aaf.model.Family;
import aaf.model.FamilyReader;
import aaf.model.Sponsor;
import aaf.model.SponsorReader;


/**
 * Servlet implementation class UploadFilesServlet
 */
@WebServlet("/UploadFilesServlet")
@MultipartConfig
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
		
	    Part filePart = request.getPart("sponsorCSV");
//	    String sponsorFileName = getFilename(filePart);
	    
	    SponsorReader sponsorReader = new SponsorReader(filePart.getInputStream());
	    LinkedList<Sponsor> sponsors = sponsorReader.createSponsorObjects();
	    
	    request.getSession().setAttribute("sponsors", sponsors);
	    
	    filePart = request.getPart("familyCSV");
	    
	    FamilyReader familyReader = new FamilyReader(filePart.getInputStream());
	    PriorityQueue<Family> families = familyReader.createFamilyObjects();
	    
	    request.getSession().setAttribute("families", families);

		
//		FileLocations fileLocs = new FileLocations(
//				sponsorFileName,
//				request.getParameter("familyEmailTextFile"),
//				request.getParameter("familyCSV"),
//				request.getParameter("sponsorCSV"),
//				request.getParameter("faqPdf"),
//				request.getParameter("familyPdfs"));
//		
//		request.getSession().setAttribute(SessionAttributeConstants.FILE_LOCS_KEY, fileLocs);
//		
//		System.out.println("update servlet file locs "  + fileLocs);
//		
//		FileLocations fileLocs2 = (FileLocations) request.getSession().getAttribute(SessionAttributeConstants.FILE_LOCS_KEY);
//		System.out.println("update servlet file locs again "  + fileLocs2);
		request.getRequestDispatcher("aaf3_family_matching.html").forward(request, response);
		
	}
	
	private static String getFilename(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}

}
