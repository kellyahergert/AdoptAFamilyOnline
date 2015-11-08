package aaf.model;
import java.io.Serializable;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;

import aaf.model.SponsorEntry.FamilyType;

@Entity
public class Sponsor extends Person implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	int sponId;
	int numLargeFams;
	int numMediumFams;
	int numSmallFams;
	HashMap<Family,FamilyType> adoptedFams = new HashMap<Family,FamilyType>();

	public Sponsor(int sponId, String firstName, String lastName, String emailAddress,
			int numLargeFams, int numMediumFams,
			int numSmallFams) {
		super(firstName, lastName, emailAddress);
		this.sponId = sponId;
		this.numLargeFams = numLargeFams;
		this.numMediumFams = numMediumFams;
		this.numSmallFams = numSmallFams;
	}
	
	/**
	 * objectdb seems to need a default constructor
	 */
	public Sponsor()
	{
		super("", "", "");
		this.sponId = 0;
		this.numLargeFams = 0;
		this.numMediumFams = 0;
		this.numSmallFams = 0;	
	}

	public void addAdoptedFam(FamilyType famType, Family adoptedFamily) {
		adoptedFams.put(adoptedFamily,famType);
	}
	
	public int getSponId()
	{
		return sponId;
	}

	public int getNumLargeFams() {
		return numLargeFams;
	}

	public int getNumMediumFams() {
		return numMediumFams;
	}

	public int getNumSmallFams() {
		return numSmallFams;
	}

	public HashMap<Family, FamilyType> getAdoptedFams() {
		return adoptedFams;
	}

	public String toString(){
		return this.getSponId() + "," +
			   this.getFirstName() + "," + 
	           this.getLastName() + "," + 
			   this.getEmailAddress() + "," +
	           this.getNumSmallFams() + "," +
			   this.getNumMediumFams() + "," +
	           this.getNumLargeFams();
			
	}

}
