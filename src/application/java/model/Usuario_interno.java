package application.java.model;

/**
 * Class that defines the structure of Usuario_interno-type object (DAO design pattern
 * is applied). It represents the model.
 */
public class Usuario_interno {

	private String user;
	private String pass;
	private byte admin;

	public Usuario_interno(String user, String pass, byte admin) {
		this.user = user;
		this.pass = pass;
		this.admin = admin;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public byte getAdmin() {
		return admin;
	}

}