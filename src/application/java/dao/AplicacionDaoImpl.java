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

import application.java.model.Aplicacion;

/**
 * Class that implements the methods defined in AplicacionDao (DAO design
 * pattern is applied)
 */
public class AplicacionDaoImpl implements AplicacionDao {

	private Connection connection;

	// Database credentials
	final String DB_URL;
	final String DB_USER;
	final String DB_PASS;

	public AplicacionDaoImpl() {
		Properties properties = new Properties();
		try {
			//properties.load(new FileInputStream(new File("src/application/resources/conf/credentials.properties")));
			properties.load(new FileInputStream(new File("conf/credentials.properties")));
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
	public List<Aplicacion> getAllAplicaciones() {
		return getAplicaciones("", "", "", Byte.MIN_VALUE);
	}

	@Override
	public List<Aplicacion> getAplicaciones(String id, String descripcion, String gestor, byte servidor) {
		List<Aplicacion> aplicaciones = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing aplicaciones
			String query = "SELECT * FROM Aplicacion WHERE 1 = 1";
			if (!id.isBlank()) {
				query += " AND ID LIKE '%" + id + "%'";
			}
			if (!descripcion.isBlank()) {
				query += " AND Descripcion LIKE '%" + descripcion + "%'";
			}
			if (!gestor.isBlank()) {
				query += " AND Gestor LIKE '%" + gestor + "%'";
			}
			if (servidor != Byte.MIN_VALUE) {
				query += " AND Servidor = " + servidor;
			}

			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				aplicaciones.add(new Aplicacion(rs.getString(1), rs.getString(2), rs.getString(3),
						Byte.valueOf(rs.getString(4))));
			}
			connection.close();
			return aplicaciones;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return aplicaciones;
	}

	@Override
	public boolean updateAplicacion(String id, Aplicacion aplicacion) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "UPDATE Aplicacion SET ID = '" + aplicacion.getId() + "', Descripcion = '"
					+ aplicacion.getDescripcion() + "', Gestor = '" + aplicacion.getGestor() + "', Servidor = "
					+ aplicacion.getServidor() + " WHERE ID = '" + id + "'";
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
	public boolean insertAplicacion(Aplicacion aplicacion) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "INSERT INTO Aplicacion (ID, Descripcion, Gestor, Servidor) VALUES ('" + aplicacion.getId()
					+ "', '" + aplicacion.getDescripcion() + "', '" + aplicacion.getGestor() + "', "
					+ aplicacion.getServidor() + ")";
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
	public boolean deleteAplicaciones(String id, String descripcion, String gestor, byte servidor) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			String query = "DELETE FROM Aplicacion WHERE 1 = 1";
			if (!id.isBlank()) {
				query += " AND ID = '" + id + "'";
			} else {
				if (!descripcion.isBlank()) {
					query += " AND Descripcion LIKE '%" + descripcion + "%'";
				}
				if (!gestor.isBlank()) {
					query += " AND Gestor LIKE '%" + gestor + "%'";
				}
				if (servidor != Byte.MIN_VALUE) {
					query += " AND Servidor = " + servidor;
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
