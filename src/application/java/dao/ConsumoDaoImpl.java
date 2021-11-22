package application.java.dao;

import java.sql.Connection;
import java.util.List;
import application.java.model.Aplicacion;
import application.java.model.Consumo;
import application.java.model.Mes;
import application.java.model.Usuario;

/**
 * Class that implements the methods defined in AplicacionDao (DAO design
 * pattern is applied)
 */
public class ConsumoDaoImpl implements ConsumoDao {

	private Connection connection;

	// Database information
	static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_consumos";
	static final String DB_USER = "root";
	static final String DB_PASS = "root";
	
	@Override
	public List<Consumo> getAllConsumos() {
		
		return null;
	}
	@Override
	public List<Consumo> getConsumos(int id, Usuario usuario, Aplicacion aplicacion, Mes mes, int consumo) {
		
		return null;
	}
	@Override
	public boolean insertConsumo(Consumo consumo) {
		
		return false;
	}
	@Override
	public boolean deleteConsumos(int id, Usuario usuario, Aplicacion aplicacion, Mes mes, int consumo) {
		
		return false;
	}
	@Override
	public boolean updateConsumo(int id, Consumo consumo) {
		
		return false;
	}

}
