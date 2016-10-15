package aaf.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.Family;
import aaf.model.FileWriter;
import aaf.model.Sponsor;
import aaf.model.SponsorEntry;
import aaf.model.SponsorEntry.FamilyType;

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
	    	int smallFamMax = Integer.parseInt(request.getParameter("smallFamMax"));
			int mediumFamMin = Integer.parseInt(request.getParameter("mediumFamMin"));
			int mediumFamMax = Integer.parseInt(request.getParameter("mediumFamMax"));
			int largeFamMin = Integer.parseInt(request.getParameter("largeFamMin"));

			SponsorEntry tempEntry = null;
			Family tempFamily = null;
			
			String storeDir = (String) request.getSession().getAttribute("storeDir");
			FileWriter matchWriter = new FileWriter(storeDir + "/MatchedFamilies.csv");
			
			matchWriter.writeToFile(SponsorEntry.getHeader() + "," + Family.getHeader());

			
			//let the matching begin!!!!
//			while (!familiesToAdopt.isEmpty() && !sponsorEntries.isEmpty()){
//				tempFamily = familiesToAdopt.poll();
//				tempEntry = sponsorEntries.poll();
//				
//				matchWriter.writeToFile("\n" + tempEntry + "," + 
//		                   tempFamily.toString());
//				
//				tempEntry.getSponsor().addAdoptedFam(tempEntry.getFamType(), tempFamily);
//			}

			 PriorityQueue<Family> familiesNotAdopted = new PriorityQueue<Family>();

			//let the matching begin!!!!
			while (!familiesToAdopt.isEmpty())
			{
				tempFamily = familiesToAdopt.poll();
				System.out.println("Family " + tempFamily.getNumFamilyMembers() + " looking for match");
				// TODO: if LARGE sponsor is too big for family, try MEDIUM, then SMALL
				tempEntry = sponsorEntries.peek();

				System.out.println("  Try Sponsor " + tempEntry.getFamType());
				if ((tempEntry.getFamType() == FamilyType.SMALL && tempFamily.getNumFamilyMembers() <= smallFamMax) ||
					(tempEntry.getFamType() == FamilyType.MEDIUM && (tempFamily.getNumFamilyMembers() >= mediumFamMin &&
																	tempFamily.getNumFamilyMembers() <= mediumFamMax)) ||
					(tempEntry.getFamType() == FamilyType.LARGE && tempFamily.getNumFamilyMembers() >= largeFamMin))
				{
					//TODO print out csv after this while loop and after
					// sorting sponsors by id
					System.out.println("found a match!");
					matchWriter.writeToFile("\n" + tempEntry + "," + 
							tempFamily.toString());
					
					tempEntry.getSponsor().addAdoptedFam(tempFamily);
					sponsorEntries.poll();
				}
				else
				{
					System.out.println("not adopted");
					familiesNotAdopted.add(tempFamily);
				}
				
			}
			
			matchWriter.close();
			
			System.out.println(familiesNotAdopted.size() + " families not adopted");
			
			// save leftover families and sponsor entries for sending rejection emails
			request.getSession().setAttribute("unmatchedFamilies", familiesNotAdopted);
			request.getSession().setAttribute("unmatchedSponsors", sponsorEntries);
		
			if (!familiesNotAdopted.isEmpty())
			{
				FileWriter unmatchedWriter = new FileWriter(storeDir + "/unmatchedFamilies.csv");
				unmatchedWriter.writeToFile(Family.getHeader());
				
				for (Family thisFam : familiesNotAdopted)
				{
					System.out.println(thisFam.getId() + " - " + thisFam.getFamilyName() + " family was not adopted :(");
					unmatchedWriter.writeToFile("\n" + thisFam.toString());
				}
				unmatchedWriter.close();
			}
			if (!sponsorEntries.isEmpty())
			{				
				FileWriter unmatchedWriter = new FileWriter(storeDir + "/unmatchedSponsors.csv");
				unmatchedWriter.writeToFile(SponsorEntry.getHeader());
				
				for (SponsorEntry thisSpon : sponsorEntries)
				{
					System.out.println(thisSpon.getSponsor().getSponId() + " - " +  thisSpon.getSponsor().getLastName() + " family had no family to adopt :(");
					unmatchedWriter.writeToFile("\n" + thisSpon.toString());
				}
				
				unmatchedWriter.close();
			}
	    	
	    }
	    
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
	}

}
