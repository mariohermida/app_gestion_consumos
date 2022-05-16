package application.java.model;

/**
 * Class that defines the structure of Usuario_interno-type object (DAO design pattern
 * is applied). It represents the model.
 */
public class Usuario_interno {

	private String usuario;
	private String clave;
	private byte permiso;

	public Usuario_interno(String usuario, String clave, byte permiso) {
		this.usuario = usuario;
		this.clave = clave;
		this.permiso = permiso;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getClave() {
		return clave;
	}

	public byte getPermiso() {
		return permiso;
	}

}