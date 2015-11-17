package aaf.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;



public class FamilyReader {

	InputStream familyInputStream;
	String storeDir;

	int idIndex = -1;
	int nomFirstNameIndex = -1;
	int nomLastNameIndex = -1;
	int nomEmailAddressIndex = -1;
	int familyNameIndex = -1;
	int firstNameIndex = -1;
	int lastNameIndex = -1;
	int emailAddressIndex = -1;
	int numFamMembersIndex = -1;
	static final String idText = "ID";
	static final String nomFirstNameText = "Your Name (First)";
	static final String nomLastNameText = "Your Name (Last)";
	static final String nomEmailAddressText = "Your Work E-mail";
	static final String familyNameText = "Family's Last Name";
	static final String firstNameText = "Name of Adult #1 (First)";
	static final String lastNameText = "Name of Adult #1 (Last)";
	static final String emailAddressText = "Nominated Family's E-mail";
	static final String numFamMembersText = "Total";

	public FamilyReader(InputStream familyInputStream, String storeDir) {
		super();
		this.familyInputStream = familyInputStream;
		this.storeDir = storeDir;
	}

	public PriorityQueue<Family> createFamilyObjects() throws FileNotFoundException {

		PriorityQueue<Family> families = new PriorityQueue<Family>();

		createFamilyObjects(families);

		return families;
	}

	public void createFamilyObjects(Queue<Family> families) throws FileNotFoundException {

		Scanner in = new Scanner(this.familyInputStream);
		try
		{
			String firstLine = in.nextLine();
			// loop through column headers to determine indices for each variable
			int currIndex = 0;
			for (String columnHeader : firstLine.split(","))
			{
				columnHeader = columnHeader.toLowerCase();
				if (columnHeader.contains(idText.toLowerCase())) {idIndex = currIndex;}
				if (columnHeader.contains(nomFirstNameText.toLowerCase())) {nomFirstNameIndex = currIndex;}
				if (columnHeader.contains(nomLastNameText.toLowerCase())) {nomLastNameIndex = currIndex;}
				if (columnHeader.contains(nomEmailAddressText.toLowerCase())) {nomEmailAddressIndex = currIndex;}
				if (columnHeader.contains(familyNameText.toLowerCase())) {familyNameIndex = currIndex;}
				if (columnHeader.contains(firstNameText.toLowerCase())) {firstNameIndex = currIndex;}
				if (columnHeader.contains(lastNameText.toLowerCase())) {lastNameIndex = currIndex;}
				if (columnHeader.contains(emailAddressText.toLowerCase())) {emailAddressIndex = currIndex;}
				if (columnHeader.contains(numFamMembersText.toLowerCase())) {numFamMembersIndex = currIndex;}
				++currIndex;
			}

			String failedColumns = "";
			if (idIndex == -1) {failedColumns += ("\n" + idText);}
			if (nomFirstNameIndex == -1) {failedColumns += ("\n" + nomFirstNameText);}
			if (nomLastNameIndex == -1) {failedColumns += ("\n" + nomLastNameText);}
			if (nomEmailAddressIndex == -1) {failedColumns += ("\n" + nomEmailAddressText);}
			if (familyNameIndex == -1) {failedColumns += ("\n" + familyNameText);}
			if (firstNameIndex == -1) {failedColumns += ("\n" + firstNameText);}
			if (lastNameIndex == -1) {failedColumns += ("\n" + lastNameText);}
			if (emailAddressIndex == -1) {failedColumns += ("\n" + emailAddressText);}
			if (numFamMembersIndex == -1) {failedColumns += ("\n" + numFamMembersText);}

			if (failedColumns != "")
			{
				throw new RuntimeException("Unable to find the following columns in Family csv:" + failedColumns);
			}

			int count = 0;

			while (in.hasNextLine()){
				String line = in.nextLine();

				String[] tokens = line.split(",");

				int id = Integer.parseInt(tokens[idIndex]);

				String nomFirstName = camelCaseString(tokens[nomFirstNameIndex]);
				String nomLastName = camelCaseString(tokens[nomLastNameIndex]);
				String nomEmailAddress = tokens[nomEmailAddressIndex];
				Nominator nominator = new Nominator(nomFirstName, nomLastName, nomEmailAddress);

				String familyName = camelCaseString(tokens[familyNameIndex]);
				String firstName = camelCaseString(tokens[firstNameIndex]);
				String lastName = camelCaseString(tokens[lastNameIndex]);
				String emailAddress = tokens[emailAddressIndex];
				int numFamMembers = Integer.parseInt(tokens[numFamMembersIndex]);

				String[] attachmentNames = new String[2];
				String attachmentName = "";

				for (int i=0; i < 2; i++){

					String noSpaceName = "";

					if (i == 0){
						noSpaceName = firstName + lastName;
					}else{
						noSpaceName = firstName + familyName;
					}

					noSpaceName = noSpaceName.replaceAll("\\s", "");
					attachmentNames[i] = storeDir + "/" + numFamMembers + " " + noSpaceName + ".pdf";

					File testAttachment = new File(attachmentNames[i]);
					boolean attachmentExists = testAttachment.exists();

					if (attachmentExists){
						break;

					}else if (i == 1){
						System.out.println("Family " + id + " - could not find attachment for " +
								familyName + " family");
						System.out.println("----> " + attachmentNames[0]);
						System.out.println("----> " + attachmentNames[1]);

						attachmentName = "NEED_ATTACHMENT_NAME";
						count++;
					}
				}

				Family tempFam = new Family(id, nominator, familyName, firstName, lastName,
						                    emailAddress, numFamMembers, attachmentName);

				families.add(tempFam);

			}

			System.out.println("Failed to find " + count + " attachments");
		}
		finally
		{
			in.close();
		}

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
