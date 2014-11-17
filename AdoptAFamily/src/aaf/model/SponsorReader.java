package aaf.model;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;


public class SponsorReader {

	static final int sponIdIndex = 0;
	static final int firstNameIndex = 1;
	static final int lastNameIndex = 2;
	static final int emailAddressIndex = 3;
	static final int numSmallFamsIndex = 4;
	static final int numMediumFamsIndex = 5;
	static final int numLargeFamsIndex = 6;
	
	InputStream sponserInputStream;
	PriorityQueue<SponsorEntry> sponsorEntries = new PriorityQueue<SponsorEntry>();
	
	public SponsorReader(InputStream sponserInputStream) {
		super();
		this.sponserInputStream = sponserInputStream;
	}

	public LinkedList<Sponsor> createSponsorObjects() throws FileNotFoundException{
		LinkedList<Sponsor> sponsors = new LinkedList<Sponsor>();
		
		Scanner in = new Scanner(sponserInputStream);
		
		// get rid of description line
		in.nextLine();
		
		Sponsor tempSponsor;
		SponsorEntry tempEntry;
		
		while(in.hasNextLine()){
			
			String line = in.nextLine();
			
			String[] tokens = line.split(",");
			
			int sponId = Integer.parseInt(tokens[sponIdIndex]);
			String firstName = camelCaseString(tokens[firstNameIndex]);
			String lastName = camelCaseString(tokens[lastNameIndex]);
			String emailAddress = tokens[emailAddressIndex];
			int numLargeFams = Integer.parseInt(tokens[numLargeFamsIndex]);
			int numMediumFams = Integer.parseInt(tokens[numMediumFamsIndex]);			
			int numSmallFams = Integer.parseInt(tokens[numSmallFamsIndex]);
			
			tempSponsor = new Sponsor(sponId, firstName, lastName, emailAddress, numLargeFams, numMediumFams, numSmallFams);
			
			sponsors.add(tempSponsor);
			
			for (int i=0; i < numLargeFams; i++){
				tempEntry = new SponsorEntry(SponsorEntry.FamilyType.LARGE, tempSponsor);
				sponsorEntries.add(tempEntry);
			}
			
			for (int i=0; i < numMediumFams; i++){
				tempEntry = new SponsorEntry(SponsorEntry.FamilyType.MEDIUM, tempSponsor);
				sponsorEntries.add(tempEntry);
			}
			
			for (int i=0; i < numSmallFams; i++){
				tempEntry = new SponsorEntry(SponsorEntry.FamilyType.SMALL, tempSponsor);
				sponsorEntries.add(tempEntry);
			}
		}
		
		in.close();
		
		return sponsors;
	}

	public PriorityQueue<SponsorEntry> getSponsorEntries() {
		return sponsorEntries;
	}
	
	public String camelCaseString(String input){
		//fix all lower case or all uppercase to camel-case
		if (input.matches("[a-z]*") || input.matches("[A-Z]*")){
			input = input.toLowerCase();
			String firstLetter = input.substring(0, 1);
			input = firstLetter.toUpperCase() + input.substring(1);
		}
		
		return input;
	}	
}
