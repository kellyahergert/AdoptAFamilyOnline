package aaf.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import aaf.model.Family;
import aaf.model.Nominator;
import aaf.model.Sponsor;

public class EmailConverter {

	/**
	 * main method is for testing purposes only
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException 
	{
		Sponsor sponsor = new Sponsor(1, "Chase", "Johnson", "test@test.com", 1, 2, 3);
		Scanner in = new Scanner(new FileReader("C:/aaf_test/1_MatchedSponsorEmailHTML.txt"));
		
		String text = "";
		
		while(in.hasNextLine())
		{
			text += in.nextLine();
		}
		
		in.close();
		
		String convertedText = EmailConverter.convertSponsorEmailText(text, sponsor);
		System.out.println(convertedText);
		
		Nominator nominator = new Nominator("Brooke", "Nominator", "nominator@nom.com");
		Family family = new Family(1, nominator, "Sherlock", "Katie", "SSherlock",
				"family@fam.com", 5, "test");
		
		Scanner in2 = new Scanner(new FileReader("C:/aaf_test/3_WaitListEmailHTML.txt"));
//		Scanner in2 = new Scanner(new FileReader("C:/aaf_test/2_MatchedNominatorEmailHTML.txt"));

		String text2 = "";
		
		while(in2.hasNextLine())
		{
			text2 += in2.nextLine();
		}
		
		in2.close();
		
		String convertedText2 = EmailConverter.convertNominatorEmailText(text2, family);
		System.out.println(convertedText2);
	}
	
	public static String convertSponsorEmailText(String text, Sponsor sponsor)
	{
		String familyList = "";
		String yORies = "";
		String sORnothing = "";
		String hasORhave = "";
		String one_ofORnothing = "";
		List<Family> adoptedFamilies = sponsor.getAdoptedFams();
		// Determine if words are singular or plural
		if (adoptedFamilies.size() > 1)
		{
			yORies = "ies";
			sORnothing = "s";
			hasORhave = "have";
			one_ofORnothing = "";
		}
		else
		{
			yORies = "y";
			sORnothing = "";
			hasORhave = "has";
			one_ofORnothing = "one of";
		}
		// Determine family list
		int familyNum = 0;
		for (Family family : adoptedFamilies)
		{
			familyNum += 1;
			// First family:
			if (familyNum == 1)
			{
				familyList += family.getFamilyName();
				continue;
			}
			// 2nd of only 2 families:
			if (familyNum == 2 && adoptedFamilies.size() == 2) // 2nd of 2 families
			{
				familyList += " and ";
				familyList += family.getFamilyName();
				continue;
			}
			// all other cases:
			if (familyNum < adoptedFamilies.size()) // not last family
			{
				familyList += ", ";
			}
			else // last of >2 families
			{
				familyList += ", and ";
			}
			familyList += family.getFamilyName();
		}
		// Finally, convert the email text
		String convertedSponText = text.replaceAll("\\[Person\\.FirstName\\]", sponsor.getFirstName());
		convertedSponText = convertedSponText.replaceAll("\\[FamilyList\\]", familyList);
		convertedSponText = convertedSponText.replaceAll("\\[yORies\\]", yORies);
		convertedSponText = convertedSponText.replaceAll("\\[s\\?\\]", sORnothing);
		convertedSponText = convertedSponText.replaceAll("\\[hasORhave\\]", hasORhave);
		convertedSponText = convertedSponText.replaceAll("\\[one\\_ofORnothing\\]", one_ofORnothing);
		return convertedSponText;
	}
	
	public static String convertNominatorEmailText(String text, Family family)
	{
		String convertedText = text.replaceAll("\\[Person\\.FirstName\\]", family.getNominator().getFirstName());
		convertedText = convertedText.replaceAll("\\[Family\\.FamilyName\\]", family.getFamilyName());
		convertedText = convertedText.replaceAll("\\[Family\\.Id\\]", Integer.toString(family.getId()));
		return convertedText;
	}

}