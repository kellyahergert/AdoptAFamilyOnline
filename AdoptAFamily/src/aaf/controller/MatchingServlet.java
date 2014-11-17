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
import aaf.model.FileLocations;
import aaf.model.Sponsor;

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
	    System.out.println("num of sponsors " + sponsors.size());
	    
	    PriorityQueue<Family> families = (PriorityQueue<Family>) request.getSession().getAttribute("families");
	    System.out.println("num of families " + families.size());

	    
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
	}

}
