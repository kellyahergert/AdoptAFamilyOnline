package aaf.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import aaf.model.Family;
import aaf.model.Person;
import aaf.model.Sponsor;

public class EmailConverter {

	public static void main(String[] args) throws FileNotFoundException 
	{
		Sponsor sponsor = new Sponsor(1, "Chase", "Johnson", "test@test.com", 1, 2, 3);
		Scanner in = new Scanner(new FileReader("C:/aaf_test/SponsorEmailHTML.txt"));
		
		String text = "";
		
		while(in.hasNextLine())
		{
			text += in.nextLine();
		}
		
		in.close();
		
		String convertedText = EmailConverter.convertSponsorEmailText(text, sponsor);
		System.out.println(convertedText);
		
		Person nominator = new Person("Brooke", "Nominator", "nominator@nom.com");
		Family family = new Family(1, nominator, "Sherlock", "Katie", "SSherlock",
				"family@fam.com", 5, "test");
		
		Scanner in2 = new Scanner(new FileReader("C:/aaf_test/NominatorEmailHTML.txt"));
		
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
		return text.replaceAll("\\[Person\\.FirstName\\]", sponsor.getFirstName());
	}
	
	public static String convertNominatorEmailText(String text, Family family)
	{
		String convertedText = text.replaceAll("\\[Person\\.FirstName\\]", family.getNominator().getFirstName());
		convertedText = convertedText.replaceAll("\\[Family\\.FamilyName\\]", family.getFamilyName());
		return convertedText;
	}

}