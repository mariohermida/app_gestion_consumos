package application.java.model;

/**
 * Class that defines the structure of AuditTrail-type object (DAO design
 * pattern is applied). It represents the model.
 */
public class AuditTrail {

	private String id; // Primary key
	private String tipo;
	private String tabla;
	private String accion;
	private String fechaHora;
	private String usuarioBBDD;

	public AuditTrail(String id, String tipo, String tabla, String accion, String fechaHora, String usuarioBBDD) {
		this.id = id;
		this.tipo = tipo;
		this.tabla = tabla;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.usuarioBBDD = usuarioBBDD;
	}

	public String getId() {
		return id;
	}

	public String getTipo() {
		return tipo;
	}
	
	public String getTabla() {
		return tabla;
	}

	public String getAccion() {
		return accion;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public String getUsuarioBBDD() {
		return usuarioBBDD;
	}

}