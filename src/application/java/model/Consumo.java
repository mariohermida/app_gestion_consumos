package application.java.model;

/**
 * Class that defines the structure of Aplicacion-type object (DAO design
 * pattern is applied)
 */
public class Consumo {

	private int id; // Primary key
	private Usuario usuario;
	private Aplicacion aplicacion;
	private Mes mes;
	private int consumo;

	public Consumo(int id, Usuario usuario, Aplicacion aplicacion, Mes mes, int consumo) {
		this.id = id;
		this.usuario = usuario;
		this.aplicacion = aplicacion;
		this.mes = mes;
		this.consumo = consumo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public Mes getMes() {
		return mes;
	}

	public void setMes(Mes mes) {
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
		return "Id: " + id + "; Usuario: " + usuario + "; Aplicación: " + aplicacion + "; Mes: " + mes + "; Consumo: "
				+ consumo;
	}

}
