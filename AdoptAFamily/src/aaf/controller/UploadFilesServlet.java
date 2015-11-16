package aaf.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
import aaf.model.MatchedFamiliesReader;
import aaf.model.Sponsor;
import aaf.model.SponsorEntry;
import aaf.model.SponsorReader;
import aaf.model.StorageManager;


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
		System.out.println("Email Server Credentials: " + creds);
		
		String storeDir = (String) request.getSession().getAttribute("storeDir");
		
		// get uploaded files that are common to these two buttons
		if (request.getParameter("goToAaf3") != null ||
			request.getParameter("goToAaf4UsingDb") != null)
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
		    
		    request.getSession().setAttribute("sponsorEmailText", sponsorEmailText);
		    
		    
		    // nominator waitlisted email text
		    filePart = request.getPart("nominatorWaitListEmailTextFile");
		    
		    Scanner nominatorWaitListEmail = new Scanner(filePart.getInputStream());
		    String nominatorWaitListEmailText = "";
		    
		    while (nominatorWaitListEmail.hasNextLine())
		    {
		    	nominatorWaitListEmailText += nominatorWaitListEmail.nextLine();
		    }
		    
		    nominatorWaitListEmail.close();
		    
		    request.getSession().setAttribute("nominatorWaitListedEmailText", nominatorWaitListEmailText);
		    
		    
		    // nominator email text
		    filePart = request.getPart("nominatorEmailTextFile");
		    
		    Scanner nominatorEmail = new Scanner(filePart.getInputStream());
		    String nominatorEmailText = "";
		    
		    while (nominatorEmail.hasNextLine())
		    {
		    	nominatorEmailText += nominatorEmail.nextLine();
		    }
		    
		    nominatorEmail.close();
		    
		    request.getSession().setAttribute("nominatorEmailText", nominatorEmailText);
		    
			// FAQ PDF
		    filePart = request.getPart("faqPdf");
		    
		    File storeFile = new File(storeDir + "/FAQ.pdf");
		    
		    Files.copy(filePart.getInputStream(), storeFile.toPath());
	
		    request.getSession().setAttribute("faqLoc", storeFile.toPath().toString());
		    
			
		    // Sponsor Obligation PDF
		    filePart = request.getPart("sponsorObligationsPdf");
		    
		    File sponObligationStoreFile = new File(storeDir + "/Sponsor_Obligations.pdf");
		    
		    Files.copy(filePart.getInputStream(), sponObligationStoreFile.toPath());
	
		    request.getSession().setAttribute("sponObligationLoc", sponObligationStoreFile.toPath().toString());
		}
		
		/**
		 * Upload family/sponsor csvs, email text files, faq and sponsor 
		 * obligation PDFs
		 */
		if (request.getParameter("goToAaf3") != null)
		{
		    // sponsor csv
		    Part filePart = request.getPart("sponsorCSV");
	//	    String sponsorFileName = getFilename(filePart);
		    
		    SponsorReader sponsorReader = new SponsorReader(filePart.getInputStream());
		    LinkedList<Sponsor> sponsors = sponsorReader.createSponsorObjects();
		    PriorityQueue<SponsorEntry> sponsorEntries = sponsorReader.getSponsorEntries();
		    
		    Collections.sort(sponsors);
		    
		    request.getSession().setAttribute("sponsors", sponsors);
		    request.getSession().setAttribute("sponsorEntries", sponsorEntries);
		    
		    
		    // family csv
		    filePart = request.getPart("familyCSV");
		    
		    FamilyReader familyReader = new FamilyReader(filePart.getInputStream(), storeDir);
		    PriorityQueue<Family> families = familyReader.createFamilyObjects();
	
		    request.getSession().setAttribute("families", families);
		    
		    StorageManager storageMgr = new StorageManager();
		    
		    String clearDatabase = request.getParameter("clearDatabase");
		    if (clearDatabase != null && clearDatabase.equals("on"))
		    {
		    	System.out.println("delete all data");
		    	storageMgr.deleteAllData();
		    }
		    
		    String loadDatabase = request.getParameter("loadDatabase");
		    if (loadDatabase != null && loadDatabase.equals("on"))
		    {
		    	System.out.println("store data");
		    	storageMgr.storeFamilies(families);
		    	storageMgr.storeSponsors(sponsors);
		    }
		    
		    // all files uploaded, go to next page
			request.getRequestDispatcher("aaf3_family_matching.html").forward(request, response);
		}
		
		/**
		 * Upload Family Wishlist PDFs
		 */
		else if (request.getParameter("goToAaf2_1") != null)
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
			request.getRequestDispatcher("aaf2.1_files2.html").forward(request, response);
		}
		
		/*
		 * Load families and sponsors from the database, read in matches
		 * from the provided csv.  Send emails based on matches read in
		 * 
		 */
		else if (request.getParameter("goToAaf4UsingDb") != null)
		{
	    	System.out.println("running from database and matched csv");
	    	StorageManager storageMgr = new StorageManager();
	    	Queue<Family> familiesFromDb = storageMgr.retrieveFamilies();
	    	List<Sponsor> sponsorsFromDb = storageMgr.retrieveSponsors();
	    	List<Sponsor> matchedSponsors = new LinkedList<Sponsor>();
	    	
	    	Part filePart;
	    	
	    	// if user uploaded a waitlist csv, parse it and set session attribute
	    	// so waitlist emails are sent
	    	if (request.getPart("waitlistedFamilyCsv").getSize() > 0)
	    	{
	    		filePart = request.getPart("waitlistedFamilyCsv");
				FamilyReader familyReader = new FamilyReader(filePart.getInputStream(), storeDir);
				PriorityQueue<Family> unmatchedFamilies = familyReader.createFamilyObjects();

				request.getSession().setAttribute("unmatchedFamilies", unmatchedFamilies);
	    	}
		    
		    filePart = request.getPart("matchedFamilyCsv");
		    
	    	MatchedFamiliesReader matchReader = new MatchedFamiliesReader(filePart.getInputStream());

			HashMap<Integer, LinkedList<Integer>> matchedIDs = matchReader.parseMatchedFamilies();
			
			HashMap<Integer, Family> familyMap = new HashMap<Integer, Family>();
			for (Family fam : familiesFromDb)
			{
				familyMap.put(fam.getId(), fam);
			}
			
			HashMap<Integer, Sponsor> sponsorMap = new HashMap<Integer, Sponsor>();
			for (Sponsor spon : sponsorsFromDb)
			{
				sponsorMap.put(spon.getSponId(), spon);
			}
			
			for (Integer sponId : matchedIDs.keySet())
			{
				Sponsor curSpon = sponsorMap.get(sponId);
				for (Integer famId : matchedIDs.get(sponId))
				{
					Family matchedFam = familyMap.get(famId);
					
					curSpon.addAdoptedFam(matchedFam);
				}
				matchedSponsors.add(curSpon);
			}
			
			// set session attributes for RunServlet to use
			request.getSession().setAttribute("sponsors", matchedSponsors);
			request.getSession().setAttribute("families", familiesFromDb);
			
			request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
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