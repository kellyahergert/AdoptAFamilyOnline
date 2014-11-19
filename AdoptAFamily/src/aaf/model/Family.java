package aaf.model;

import java.io.Serializable;

public class Family extends Person implements Comparable<Family>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String familyName;
	private int numFamilyMembers;
	private String attachmentName;
	private Person nominator;

	public Family(int id, Person nominator, String familyName, String firstName, String lastName,
			String emailAddress, int numFamilyMembers, String attachmentName) {
		super(firstName, lastName, emailAddress);
		this.id = id;
		this.setNominator(nominator);
		this.familyName = familyName;
		this.numFamilyMembers = numFamilyMembers;
		this.attachmentName = attachmentName;
	}

	public int getId() {
		return id;
	}

	public String getFamilyName() {
		return familyName;
	}

	public int getNumFamilyMembers() {
		return numFamilyMembers;
	}

	public String getAttachmentName() {
		return attachmentName;
	}
	
	public Person getNominator() {
		return nominator;
	}

	public void setNominator(Person nominator) {
		this.nominator = nominator;
	}

	@Override
	public int compareTo(Family fam) {

		if (this.getNumFamilyMembers() < fam.getNumFamilyMembers()) {
			return 1;
		} else if (this.getNumFamilyMembers() > fam.getNumFamilyMembers()) {
			return -1;
		} else {
			return this.getFamilyName().compareTo(fam.getFamilyName());
		}
	}

	public static String getHeader()
	{
		return "Family ID,Nominator First,Nominator Last,Nominator Email,Num in Family,Family Last,Family Email,Adult 1 First,Adult 1 Last";
	}

	public String toString(){
		return this.getId() + "," +
			   this.getNominator().getFirstName() + "," +
			   this.getNominator().getLastName() + "," +
			   this.getNominator().getEmailAddress() + "," +
			   this.getNumFamilyMembers() + "," +
	           this.getFamilyName() + "," +
			   this.getEmailAddress() + "," +
			   this.getFirstName() + "," +
			   this.getLastName();
	}



}
