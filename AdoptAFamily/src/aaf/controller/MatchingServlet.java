package aaf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aaf.model.FileLocations;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileLocations fileLocs = (FileLocations) request.getSession().getAttribute(SessionAttributeConstants.FILE_LOCS_KEY);
		System.out.println("matching servlet file locations: " + fileLocs);
		
		request.getRequestDispatcher("aaf4_run.jsp").forward(request, response);
		
	}

}
