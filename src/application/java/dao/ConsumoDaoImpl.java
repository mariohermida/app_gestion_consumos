package application.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import application.java.model.Consumo;

/**
 * Class that implements the methods defined in ConsumoDao (DAO design pattern
 * is applied)
 */
public class ConsumoDaoImpl implements ConsumoDao {

	private Connection connection;

	// Database information
	private final String DB_URL = "jdbc:mysql://localhost:3306/gestion_consumos";
	private final String DB_USER = "root";
	private final String DB_PASS = "root";

	@Override
	public List<Consumo> getAllConsumos() {
		return getConsumos(Integer.MIN_VALUE, "", "", (byte) 0, Integer.MIN_VALUE);
	}

	@Override
	public List<Consumo> getConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumo) {
		List<Consumo> consumos = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing consumos
			String query = "SELECT * FROM consumo_usuario WHERE 1 = 1";
			if (id != Integer.MIN_VALUE) {
				query += " AND ID = " + id;
			}
			if (!idUsuario.isBlank()) {
				query += " AND ID_usuario LIKE '%" + idUsuario + "%'";
			}
			if (!idAplicacion.isBlank()) {
				query += " AND ID_aplicacion LIKE '%" + idAplicacion + "%'";
			}
			if (mes != 0) {
				query += " AND Mes = " + mes;
			}
			if (consumo != Integer.MIN_VALUE) {
				query += " AND Consumo = " + consumo;
			}

			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				consumos.add(new Consumo(Integer.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3),
						Byte.valueOf(rs.getString(4)), Integer.valueOf(rs.getString(5))));
			}
			connection.close();
			return consumos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return consumos;
	}

	@Override
	public boolean insertConsumo(Consumo consumo) {

		return false;
	}

	@Override
	public boolean deleteConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumo) {

		return false;
	}

	@Override
	public boolean updateConsumo(int id, Consumo consumo) {

		return false;
	}

}
