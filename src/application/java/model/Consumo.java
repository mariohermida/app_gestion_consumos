package application.java.model;

/**
 * Class that defines the structure of Consumo-type object (DAO design pattern
 * is applied). It represents the model.
 */
public class Consumo {

	// The primary key is the combination of idUsuario, idAplicacion and mes
	private String idUsuario;
	private String idAplicacion;
	private String mes;
	private int consumo;

	public Consumo(String idUsuario, String idAplicacion, String mes, int consumo) {
		this.idUsuario = idUsuario;
		this.idAplicacion = idAplicacion;
		this.mes = mes;
		this.consumo = consumo;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public String getIdAplicacion() {
		return idAplicacion;
	}

	public String getMes() {
		return mes;
	}

	public int getConsumo() {
		return consumo;
	}

}