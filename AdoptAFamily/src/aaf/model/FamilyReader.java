package aaf.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Scanner;



public class FamilyReader {

	InputStream familyInputStream;
	
	static final int idIndex = 0;
	static final int nomFirstNameIndex = 1;
	static final int nomLastNameIndex = 2;
	static final int nomEmailAddressIndex = 3;
	static final int familyNameIndex = 5;
	static final int firstNameIndex = 7;
	static final int lastNameIndex = 8;
	static final int emailAddressIndex = 6;
	static final int phoneNumberIndex = 3;
	static final int numFamMembersIndex = 4;
	
	public FamilyReader(InputStream familyInputStream) {
		super();
		this.familyInputStream = familyInputStream;
	}

	public PriorityQueue<Family> createFamilyObjects() throws FileNotFoundException {
		
		PriorityQueue<Family> families = new PriorityQueue<Family>();
		
		Scanner in = new Scanner(this.familyInputStream);
		
		//get rid of first line - just descriptions
		in.nextLine();
		int count = 0;
		
		while (in.hasNextLine()){
			String line = in.nextLine();
			
			String[] tokens = line.split(",");
			
			int id = Integer.parseInt(tokens[idIndex]);
			
			String nomFirstName = camelCaseString(tokens[nomFirstNameIndex]);
			String nomLastName = camelCaseString(tokens[nomLastNameIndex]);
			String nomEmailAddress = tokens[nomEmailAddressIndex];
			Person nominator = new Person(nomFirstName, nomLastName, nomEmailAddress);
			
			String familyName = camelCaseString(tokens[familyNameIndex]);
			String firstName = camelCaseString(tokens[firstNameIndex]);
			String lastName = camelCaseString(tokens[lastNameIndex]);
			String emailAddress = tokens[emailAddressIndex];
			int numFamMembers = Integer.parseInt(tokens[numFamMembersIndex]);
			
			String attachmentName = "";
			
			for (int i=0; i < 2; i++){
				
				String noSpaceName = "";
				
				if (i == 0){
					noSpaceName = firstName + lastName;
				}else{
					noSpaceName = firstName + familyName;
				}
				
				noSpaceName = noSpaceName.replaceAll("\\s", "");
				attachmentName = "2013/FamilyWishLists/" + numFamMembers + " " + noSpaceName + ".pdf";
				
				File testAttachment = new File(attachmentName);
				boolean attachmentExists = testAttachment.exists();
				
				if (attachmentExists){
					break;

				}else if (i == 1){
					System.out.println("could not find attachment " + attachmentName);
					
					attachmentName = "NEED_ATTACHMENT_NAME";
					count++;
				}
			
			}
			
			Family tempFam = new Family(id, nominator, familyName, firstName, lastName, 
					                    emailAddress, numFamMembers, attachmentName);
			
			families.add(tempFam);
			
		}
		
		System.out.println("Failed to find " + count + " attachments");
		
		in.close();
		
		return families;
		
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
