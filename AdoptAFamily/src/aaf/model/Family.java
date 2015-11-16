package aaf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Family extends Person implements Comparable<Family>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	private String familyName;
	private int numFamilyMembers;
	private String attachmentName;
	private Nominator nominator;

	/**
	 * objectdb seems to need a default constructor
	 */
	public Family() {
		super("", "", "");
		this.id = 0;
		this.setNominator(null);
		this.familyName = "";
		this.numFamilyMembers = 0;
		this.attachmentName = "";
	}
	
	public Family(int id, Nominator nominator, String familyName, String firstName, String lastName,
			String emailAddress, int numFamilyMembers, String attachmentName) {
		super(firstName, lastName, emailAddress);
		this.id = id;
		this.setNominator(nominator);
		this.familyName = familyName;
		this.numFamilyMembers = numFamilyMembers;
		this.attachmentName = attachmentName;
	}

	public int getId() {
		return (int) id;
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

	public void setNominator(Nominator nominator) {
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
		return "Family ID,Your Name (First),Your Name (Last),Your Work E-mail,Total in Family,Family's Last Name,Nominated Family's E-mail,Name of Adult #1 (First),Name of Adult #1 (Last)";
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
