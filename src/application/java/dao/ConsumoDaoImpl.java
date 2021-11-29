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
		return getConsumos(Integer.MIN_VALUE, "", "", (byte) 0, 0, Integer.MAX_VALUE);
	}

	@Override
	public List<Consumo> getConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumoMin,
			int consumoMax) {
		List<Consumo> consumos = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing consumos
			String query = "SELECT * FROM consumo_usuario WHERE Consumo BETWEEN " + consumoMin + " AND " + consumoMax;
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
	public boolean updateConsumo(int id, Consumo consumo) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "UPDATE Consumo_usuario SET ID = " + consumo.getId() + ", ID_usuario = '"
					+ consumo.getIdUsuario() + "', ID_aplicacion = '" + consumo.getIdAplicacion() + "', Mes = "
					+ consumo.getMes() + ", Consumo = " + consumo.getConsumo() + " WHERE ID = " + id;
			Statement st = connection.createStatement();
			st.execute(query);
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean insertConsumo(Consumo consumo) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// By default ID is null, due to it is an autoincremental data type
			String id = "NULL";
			if (consumo.getId() != Integer.MIN_VALUE) {
				id = Integer.toString(consumo.getId());
			}
			String query = "INSERT INTO Consumo_usuario (ID, ID_usuario, ID_aplicacion, Mes, Consumo) VALUES (" + id
					+ ", '" + consumo.getIdUsuario() + "', '" + consumo.getIdAplicacion() + "', " + consumo.getMes()
					+ ", " + consumo.getConsumo() + ")";
			Statement st = connection.createStatement();
			st.execute(query);
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumoMin,
			int consumoMax) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			String query = "DELETE FROM consumo_usuario WHERE";
			if (id != Integer.MIN_VALUE) {
				query += " ID = " + id;
			} else {
				query += " Consumo BETWEEN " + consumoMin + " AND " + consumoMax;
				if (!idUsuario.isBlank()) {
					query += " AND ID_usuario LIKE '%" + idUsuario + "%'";
				}
				if (!idAplicacion.isBlank()) {
					query += " AND ID_aplicacion LIKE '%" + idAplicacion + "%'";
				}
				if (mes != 0) {
					query += " AND Mes = " + mes;
				}
			}
			Statement st = connection.createStatement();
			st.execute(query);
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
