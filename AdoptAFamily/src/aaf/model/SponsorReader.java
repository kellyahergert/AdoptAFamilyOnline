package aaf.model;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;


public class SponsorReader {

	int sponIdIndex = -1;
	int firstNameIndex = -1;
	int lastNameIndex = -1;
	int emailAddressIndex = -1;
	int numSmallFamsIndex = -1;
	int numMediumFamsIndex = -1;
	int numLargeFamsIndex = -1;
	static final String sponIdText = "ID";
	static final String firstNameText = "First";
	static final String lastNameText = "Last";
	static final String emailAddressText = "E-mail";
	static final String numSmallFamsText = "Small";
	static final String numMediumFamsText = "Medium";
	static final String numLargeFamsText = "Large";

	InputStream sponserInputStream;
	PriorityQueue<SponsorEntry> sponsorEntries = new PriorityQueue<SponsorEntry>();

	public SponsorReader(InputStream sponserInputStream) {
		super();
		this.sponserInputStream = sponserInputStream;
	}

	public LinkedList<Sponsor> createSponsorObjects() throws FileNotFoundException{
		LinkedList<Sponsor> sponsors = new LinkedList<Sponsor>();

		Scanner in = new Scanner(sponserInputStream);
		try
		{
			String firstLine = in.nextLine();
			// loop through column headers to determine indices for each variable
			int currIndex = 0;
			for (String columnHeader : firstLine.split(","))
			{
				columnHeader = columnHeader.toLowerCase();
				if (columnHeader.contains(sponIdText.toLowerCase())) {sponIdIndex = currIndex;}
				if (columnHeader.contains(firstNameText.toLowerCase())) {firstNameIndex = currIndex;}
				if (columnHeader.contains(lastNameText.toLowerCase())) {lastNameIndex = currIndex;}
				if (columnHeader.contains(emailAddressText.toLowerCase())) {emailAddressIndex = currIndex;}
				if (columnHeader.contains(numSmallFamsText.toLowerCase())) {numSmallFamsIndex = currIndex;}
				if (columnHeader.contains(numMediumFamsText.toLowerCase())) {numMediumFamsIndex = currIndex;}
				if (columnHeader.contains(numLargeFamsText.toLowerCase())) {numLargeFamsIndex = currIndex;}
				++currIndex;
			}

			String failedColumns = "";
			if (sponIdIndex == -1) {failedColumns += ("\n" + sponIdText);}
			if (firstNameIndex == -1) {failedColumns += ("\n" + firstNameText);}
			if (lastNameIndex == -1) {failedColumns += ("\n" + lastNameText);}
			if (emailAddressIndex == -1) {failedColumns += ("\n" + emailAddressText);}
			if (numSmallFamsIndex == -1) {failedColumns += ("\n" + numSmallFamsText);}
			if (numMediumFamsIndex == -1) {failedColumns += ("\n" + numMediumFamsText);}
			if (numLargeFamsIndex == -1) {failedColumns += ("\n" + numLargeFamsText);}

			if (failedColumns != "")
			{
				throw new RuntimeException("Unable to find the following columns in Sponsor csv:" + failedColumns);
			}

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
		}
		finally
		{
			in.close();
		}
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
