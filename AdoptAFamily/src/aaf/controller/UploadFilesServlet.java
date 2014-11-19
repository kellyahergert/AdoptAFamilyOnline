package aaf.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import aaf.model.EmailServerCredentials;
import aaf.model.Family;
import aaf.model.FamilyReader;
import aaf.model.Sponsor;
import aaf.model.SponsorEntry;
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
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		System.out.println("file upload servlet " + creds);
		
		
		Enumeration<String> params = request.getParameterNames();
		
		while (params.hasMoreElements())
		{
			String param = params.nextElement();
			
			System.out.println(param + " : " + request.getParameter(param));
		}
		
		String storeDir = (String) request.getSession().getAttribute("storeDir");
		
		if (request.getParameter("uploadFiles") != null)
		{
		
		    Part filePart;
		    
		    // sponsor email text
		    filePart = request.getPart("sponsorEmailTextFile");
		    
		    Scanner sponsorEmail = new Scanner(filePart.getInputStream());
		    String sponsorEmailText = "";
		    
		    while (sponsorEmail.hasNextLine())
		    {
		    	sponsorEmailText += sponsorEmail.nextLine();
		    }
		    
		    sponsorEmail.close();
		    
		    System.out.println("spons email text " + sponsorEmailText);
		    
		    request.getSession().setAttribute("sponsorEmailText", sponsorEmailText);
		    
		    
		    // nominator email text
		    filePart = request.getPart("nominatorEmailTextFile");
		    
		    Scanner nominatorEmail = new Scanner(filePart.getInputStream());
		    String nominatorEmailText = "";
		    
		    while (nominatorEmail.hasNextLine())
		    {
		    	nominatorEmailText += nominatorEmail.nextLine();
		    }
		    
		    nominatorEmail.close();
		    
		    System.out.println("nom email text " + nominatorEmailText);
	
		    request.getSession().setAttribute("nominatorEmailText", nominatorEmailText);
		    
		    // sponsor csv
		    filePart = request.getPart("sponsorCSV");
	//	    String sponsorFileName = getFilename(filePart);
		    
		    SponsorReader sponsorReader = new SponsorReader(filePart.getInputStream());
		    LinkedList<Sponsor> sponsors = sponsorReader.createSponsorObjects();
		    PriorityQueue<SponsorEntry> sponsorEntries = sponsorReader.getSponsorEntries();
		    
		    request.getSession().setAttribute("sponsors", sponsors);
		    request.getSession().setAttribute("sponsorEntries", sponsorEntries);
		    
		    
		    // family csv
		    filePart = request.getPart("familyCSV");
		    
		    FamilyReader familyReader = new FamilyReader(filePart.getInputStream());
		    PriorityQueue<Family> families = familyReader.createFamilyObjects();
	
		    request.getSession().setAttribute("families", families);
		    
		    
		    // FAQ PDF
		    filePart = request.getPart("faqPdf");
		    
		    File storeFile = new File(storeDir + "/FAQ.pdf");
		    
		    Files.copy(filePart.getInputStream(), storeFile.toPath());
	
		    request.getSession().setAttribute("faqLoc", storeFile.toPath());
		    
			request.getRequestDispatcher("aaf2_files.html").forward(request, response);
	    
		}
		else if (request.getParameter("goToAaf3") != null)
		{
		    // wishlist pdf
		    Collection<Part> fileList = request.getParts();


		    for (Part file : fileList)
		    {
		    	String name = getFilename(file);
		    	
		    	if (name != null && !name.equals(""))
		    	{
				    File storeFile = new File(storeDir + "/" + getFilename(file));
				    Files.copy(file.getInputStream(), storeFile.toPath());
		    	}
		    }
		    
		    

	
	//	    request.getSession().setAttribute("faqLoc", storeDir.toPath());
		    
			
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