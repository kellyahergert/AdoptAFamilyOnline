package aaf.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class MatchedFamiliesReader {

	InputStream inputStream;

	public static void main(String[] args) throws FileNotFoundException {
		InputStream in = new FileInputStream(new File("C:\\AAF\\2015_11_08_21_01_59\\MatchedFamilies.csv"));
		MatchedFamiliesReader matchReader = new MatchedFamiliesReader(in);
		HashMap<Integer, LinkedList<Integer>> matches = matchReader.parseMatchedFamilies();
		
		System.out.println(matches);
		
	}

	public MatchedFamiliesReader(InputStream matchedInputStream) {
		super();
		this.inputStream = matchedInputStream;
	}
	
	public HashMap<Integer, LinkedList<Integer>> parseMatchedFamilies()
	{
		Scanner in = new Scanner(this.inputStream);
		HashMap<Integer, LinkedList<Integer>> matches = new HashMap<Integer, LinkedList<Integer>>();
		try
		{
			String firstLine = in.nextLine();
			
			int sponsorIdIndex = -1;
			int familyIdIndex = -1;
			int currIndex = 0;
			
			// loop through each column header to determine indices for IDs
			for (String columnHeader : firstLine.split(","))
			{
				if (columnHeader.equalsIgnoreCase("Sponsor ID"))
				{
					sponsorIdIndex = currIndex;
				}
				else if(columnHeader.equalsIgnoreCase("Family ID"))
				{
					familyIdIndex = currIndex;
				}
				++currIndex;
			}
			if (sponsorIdIndex == -1)
			{
				throw new RuntimeException("Could not find \"Sponsor ID\" in matched families csv");
			}
			if (familyIdIndex == -1)
			{
				throw new RuntimeException("Could not find \"Family ID\" in matched families csv");
			}
			
			while (in.hasNext())
			{
				String[] split = in.nextLine().split(",");
				Integer sponsorId = Integer.parseInt(split[sponsorIdIndex]);
				Integer familyId = Integer.parseInt(split[familyIdIndex]);
				if (!matches.containsKey(sponsorId))
				{
					matches.put(sponsorId, new LinkedList<Integer>());
				}
				
				matches.get(sponsorId).add(familyId);
			}
		}
		finally
		{
			in.close();
		}
		return matches;
	}
	
}
