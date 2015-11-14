package aaf.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Nominator extends Person{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private int NomId;
	
	public Nominator(String firstName, String lastName, String emailAddress) {
		super(firstName, lastName, emailAddress);
	}
	
	/**
	 * objectdb seems to need a default constructor
	 */
	public Nominator()
	{
		super("tempFirst", "tempLast", "tempEmail");
	}
	
	
}
