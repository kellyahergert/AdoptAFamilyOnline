package aaf.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import aaf.model.Family;
import aaf.model.Person;
import aaf.model.Sponsor;

public class EmailConverter {

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
		
		Person nominator = new Person("Brooke", "Nominator", "nominator@nom.com");
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
//		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//		Date date = new Date();
//		System.out.println(dateFormat.format(date));
//		File storeDir = new File("C:/AAF/" + dateFormat.format(date) + "/");
//		System.out.println(storeDir.mkdirs());
		
	}
	
	public static String convertSponsorEmailText(String text, Sponsor sponsor)
	{
		return text.replaceAll("\\[Person\\.FirstName\\]", sponsor.getFirstName());
	}
	
	public static String convertNominatorEmailText(String text, Family family)
	{
		String convertedText = text.replaceAll("\\[Person\\.FirstName\\]", family.getNominator().getFirstName());
		convertedText = convertedText.replaceAll("\\[Family\\.FamilyName\\]", family.getFamilyName());
		convertedText = convertedText.replaceAll("\\[Family\\.Id\\]", Integer.toString(family.getId()));
		return convertedText;
	}

}