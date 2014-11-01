package aaf.model;

public class EmailServerCredentials {

	protected String host;
	protected String username;
	protected String password;
	
	public EmailServerCredentials(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	
	
}
