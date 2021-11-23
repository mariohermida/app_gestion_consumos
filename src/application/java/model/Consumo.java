package application.java.model;

/**
 * Class that defines the structure of Consumo-type object (DAO design
 * pattern is applied)
 */
public class Consumo {

	private int id; // Primary key
	private String idUsuario;
	private String idAplicacion;
	private byte mes;
	private int consumo;

	public Consumo(int id, String idUsuario, String idAplicacion, byte mes, int consumo) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idAplicacion = idAplicacion;
		this.mes = mes;
		this.consumo = consumo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public byte getMes() {
		return mes;
	}

	public void setMes(byte mes) {
		this.mes = mes;
	}

	public int getConsumo() {
		return consumo;
	}

	public void setConsumo(int consumo) {
		this.consumo = consumo;
	}

	@Override
	public String toString() {
		return "Id: " + id + "; Usuario: " + idUsuario + "; Aplicación: " + idAplicacion + "; Mes: " + mes + "; Consumo: "
				+ consumo;
	}

}
