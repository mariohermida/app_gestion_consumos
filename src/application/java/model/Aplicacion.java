package application.java.model;

/**
 * Class that defines the structure of Aplicacion-type object (DAO design
 * pattern is applied)
 */
public class Aplicacion {

	private String id; // Primary key
	private String descripcion;
	private String gestor;
	private byte servidor;

	public Aplicacion(String id, String descripcion, String gestor, byte servidor) {
		this.id = id;
		this.descripcion = descripcion;
		this.gestor = gestor;
		this.servidor = servidor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

	public byte getServidor() {
		return servidor;
	}

	public void setServidor(byte servidor) {
		this.servidor = servidor;
	}

	@Override
	public String toString() {
		return "Id: " + id + "; Descripción: " + descripcion + "; Gestor: " + gestor + "; Servidor: " + servidor;
	}

}
