package aaf.model;
import java.util.HashMap;
import java.util.LinkedList;

import aaf.model.SponsorEntry.FamilyType;

public class Sponsor extends Person{

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
