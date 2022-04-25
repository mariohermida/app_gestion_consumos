package application.java.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import application.java.model.Consumo;

/**
 * Class that implements the methods defined in ConsumoDao (DAO design pattern
 * is applied)
 */
public class ConsumoDaoImpl implements ConsumoDao {

	private Connection connection;

	// Database credentials
	final String DB_URL;
	final String DB_USER;
	final String DB_PASS;

	public ConsumoDaoImpl() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File("src/application/resources/conf/credentials.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DB_URL = properties.getProperty("url");
		DB_USER = properties.getProperty("db_user");
		DB_PASS = properties.getProperty("db_pass");
	}

	@Override
	public List<Consumo> getAllConsumos() {
		return getConsumos("", "", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public List<Consumo> getConsumos(String idUsuario, String idAplicacion, String mes, int consumoMin,
			int consumoMax) {
		List<Consumo> consumos = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing consumos
			String query = "SELECT * FROM Consumo_usuario WHERE Consumo BETWEEN " + consumoMin + " AND " + consumoMax;
			if (!idUsuario.isBlank()) {
				query += " AND ID_usuario = '" + idUsuario + "'";
			}
			if (!idAplicacion.isBlank()) {
				query += " AND ID_aplicacion = '" + idAplicacion + "'";
			}
			if (!mes.isBlank()) {
				query += " AND Mes = '" + mes + "'";
			}

			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				consumos.add(new Consumo(rs.getString(1), rs.getString(2), rs.getString(3),
						Integer.valueOf(rs.getString(4))));
			}
			connection.close();
			return consumos;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return consumos;
	}

	@Override
	public boolean updateConsumo(String oldIdUsuario, String oldIdAplicacion, String oldMes, Consumo consumo) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "UPDATE Consumo_usuario SET ID_usuario = '" + consumo.getIdUsuario() + "', ID_aplicacion = '"
					+ consumo.getIdAplicacion() + "', Mes = '" + consumo.getMes() + "', Consumo = "
					+ consumo.getConsumo() + " WHERE ID_usuario = '" + oldIdUsuario + "' AND ID_aplicacion = '"
					+ oldIdAplicacion + "' AND Mes = '" + oldMes + "'";
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
			String query = "INSERT INTO Consumo_usuario (ID_usuario, ID_aplicacion, Mes, Consumo) VALUES ('"
					+ consumo.getIdUsuario() + "', '" + consumo.getIdAplicacion() + "', '" + consumo.getMes() + "', "
					+ consumo.getConsumo() + ")";
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
	public boolean deleteConsumos(String idUsuario, String idAplicacion, String mes, int consumoMin, int consumoMax) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			String query = "DELETE FROM Consumo_usuario WHERE";
			query += " Consumo BETWEEN " + consumoMin + " AND " + consumoMax;
			if (!idUsuario.isBlank()) {
				query += " AND ID_usuario ='" + idUsuario + "'";
			}
			if (!idAplicacion.isBlank()) {
				query += " AND ID_aplicacion = '" + idAplicacion + "'";
			}
			if (!mes.isBlank()) {
				query += " AND Mes = '" + mes + "'";
			}
			Statement st = connection.createStatement();
			st.execute(query);
			connection.close();
			return true;
		} catch (

		Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
