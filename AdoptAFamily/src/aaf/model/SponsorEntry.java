package aaf.model;

public class SponsorEntry implements Comparable<SponsorEntry>{

	public enum FamilyType{LARGE, MEDIUM, SMALL}
	
	FamilyType famType;
	Sponsor sponsor;
	
	
	
	public SponsorEntry(FamilyType famType, Sponsor sponsor) {
		super();
		this.famType = famType;
		this.sponsor = sponsor;
	}

	public FamilyType getFamType() {
		return famType;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	@Override
	public int compareTo(SponsorEntry otherSponsorEntry) {

		return this.famType.compareTo(otherSponsorEntry.famType);
	}
	
	public static String getHeader()
	{
		return "Sponsor ID,Sponsor First,Sponsor Last,Sponsor Email,Family Size,Num Small,Num Med,Num Large";
	}

	@Override
	public String toString() {
		return sponsor.getSponId() + "," +
			   sponsor.getFirstName() + "," +
			   sponsor.getLastName() + "," +
			   sponsor.getEmailAddress() + "," +
			   famType + "," +
			   sponsor.getNumSmallFams() + "," +
			   sponsor.getNumMediumFams() + "," +
			   sponsor.getNumLargeFams();
	}
	
}
