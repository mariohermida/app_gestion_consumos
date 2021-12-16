package application.java.model;

/**
 * Class that defines the structure of Usuario-type object (DAO design
 * pattern is applied). It represents the model.
 */
public class Usuario {

	private String id;
	private String nombre;
	private String apellidos;
	private String email;
	private String telefono;
	private String direccion;

	public Usuario(String id, String nombre, String apellidos, String email, String telefono, String direccion) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.telefono = telefono;
		this.direccion = direccion;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getDireccion() {
		return direccion;
	}

}