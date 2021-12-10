package application.java.model;

/**
 * Class that defines the structure of AuditTrail-type object (DAO design
 * pattern is applied)
 */
public class AuditTrail {

	private String id; // Primary key
	private String tipo;
	private String accion;
	private String fechaHora;
	private String usuarioBBDD;

	public AuditTrail(String id, String tipo, String accion, String fechaHora, String usuarioBBDD) {
		this.id = id;
		this.tipo = tipo;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.usuarioBBDD = usuarioBBDD;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getUsuarioBBDD() {
		return usuarioBBDD;
	}

	public void setUsuarioBBDD(String usuarioBBDD) {
		this.usuarioBBDD = usuarioBBDD;
	}

	@Override
	public String toString() {
		return "Id: " + id + "; Tipo: " + tipo + "; Acción: " + accion + "; Marca de tiempo: " + fechaHora
				+ "; Usuario BBDD: " + usuarioBBDD;
	}

}