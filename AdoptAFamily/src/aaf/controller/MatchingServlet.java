package aaf.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.EmailServerCredentials;
import aaf.model.Family;
import aaf.model.FamilyReader;
import aaf.model.FileLocations;
import aaf.model.FileWriter;
import aaf.model.Sponsor;
import aaf.model.SponsorEntry;
import aaf.model.SponsorReader;

/**
 * Servlet implementation class MatchingServlet
 */
@WebServlet("/MatchingServlet")
public class MatchingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatchingServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmailServerCredentials creds = (EmailServerCredentials) request.getSession().getAttribute(SessionAttributeConstants.SERVER_CREDS_KEY);
		System.out.println("matching servlet " + creds);
		
//		FileLocations fileLocs = (FileLocations) request.getSession().getAttribute(SessionAttributeConstants.FILE_LOCS_KEY);
//		System.out.println("matching servlet file locations: " + fileLocs);
		
	    LinkedList<Sponsor> sponsors = (LinkedList<Sponsor>) request.getSession().getAttribute("sponsors");
	    PriorityQueue<SponsorEntry> sponsorEntries = (PriorityQueue<SponsorEntry>) request.getSession().getAttribute("sponsorEntries");
	    System.out.println("num of sponsors " + sponsors.size());
	    
	    PriorityQueue<Family> families = (PriorityQueue<Family>) request.getSession().getAttribute("families");
	    
	    // create a copy of the families so we can poll()
	    PriorityQueue<Family> familiesToAdopt = new PriorityQueue<Family>();
	    
	    for (Family fam : families)
	    {
	    	familiesToAdopt.add(fam);
	    }
	    
	    
	    System.out.println("num of families " + familiesToAdopt.size());
	    
	    if (request.getParameter("createMatches") != null)
	    {
			SponsorEntry tempEntry;
			Family tempFamily;
			
			FileWriter matchWriter = new FileWriter("C:/AAF/MatchedFamilies.csv");
			
			matchWriter.writeToFile(SponsorEntry.getHeader() + "," + Family.getHeader());

			
			//let the matching begin!!!!
			while (!familiesToAdopt.isEmpty() && !sponsorEntries.isEmpty()){
				tempFamily = familiesToAdopt.poll();
				tempEntry = sponsorEntries.poll();
							
				matchWriter.writeToFile("\n" + tempEntry + "," + 
		                   tempFamily.toString());
				
				tempEntry.getSponsor().addAdoptedFam(tempEntry.getFamType(), tempFamily);
			}
			
			matchWriter.close();
			
			System.out.println(familiesToAdopt.size() + " families not adopted");
			
			Family thisFam;
			while (!familiesToAdopt.isEmpty()){
				thisFam = familiesToAdopt.poll();
				System.out.println(thisFam.getId() + " - " + thisFam.getFamilyName() + " family was not adopted :(");
			}
			
			SponsorEntry thisSpon;
			while (!sponsorEntries.isEmpty()){
				thisSpon = sponsorEntries.poll();
				System.out.println(thisSpon.getSponsor().getSponId() + " - " +  thisSpon.getSponsor().getLastName() + " family had no family to adopt :(");
			}
	    	
	    }

	    
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
	}

}