package aaf.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class FileLocations implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String sponsorEmailTextFile;
	protected String familyEmailTextFile;
	protected String familyCSV;
	protected String sponsorCSV;
	protected String faqPdf;
	protected String familyPdfs;

	
	
	public FileLocations(String sponsorEmailTextFile, String familyEmailTextFile, String familyCSV, String sponsorCSV,
			String faqPdf, String familyPdfs) {
		this.sponsorEmailTextFile = sponsorEmailTextFile;
		this.familyEmailTextFile = familyEmailTextFile;
		this.familyCSV = familyCSV;
		this.sponsorCSV = sponsorCSV;
		this.faqPdf = faqPdf;
		this.familyPdfs = familyPdfs;
	}
	public String getSponsorEmailTextFile() {
		return sponsorEmailTextFile;
	}
	public String getFamilyEmailTextFile() {
		return familyEmailTextFile;
	}
	public String getFamilyCSV() {
		return familyCSV;
	}
	public String getSponsorCSV() {
		return sponsorCSV;
	}
	public String getFaqPdf() {
		return faqPdf;
	}
	public String getFamilyPdfs() {
		return familyPdfs;
	}
	@Override
	public String toString() {
		return "FileLocations [sponsorEmailTextFile=" + sponsorEmailTextFile + ", familyEmailTextFile="
				+ familyEmailTextFile + ", familyCSV=" + familyCSV + ", sponsorCSV=" + sponsorCSV + ", faqPdf="
				+ faqPdf + ", familyPdfs=" + familyPdfs + "]";
	}
	
	
}
