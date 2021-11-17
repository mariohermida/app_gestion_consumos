package application.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import application.java.model.Aplicacion;

/**
 * Class that implements the methods defined in AplicacionDao (DAO design
 * pattern is applied)
 */
public class AplicacionDaoImpl implements AplicacionDao {

	private Connection connection;

	// Database information
	static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_consumos";
	static final String DB_USER = "root";
	static final String DB_PASS = "root";

	@Override
	public List<Aplicacion> getAllAplicaciones() {
		List<Aplicacion> aplicaciones = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "SELECT * FROM aplicacion";
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
	public List<Aplicacion> getAplicaciones(String id, String descripcion, String gestor, byte servidor) {
		List<Aplicacion> aplicaciones = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Depending on the fields given a different query is created
			String query;
			if (id.isBlank() && descripcion.isBlank() && gestor.isBlank()) { // Only servidor
				query = "SELECT * FROM Aplicacion WHERE Servidor = '" + servidor + "'";
			} else if (id.isBlank() && descripcion.isBlank() && servidor == Byte.MIN_VALUE) { // Only gestor
				query = "SELECT * FROM Aplicacion WHERE Gestor = '" + gestor + "'";
			} else if (id.isBlank() && gestor.isBlank() && servidor == Byte.MIN_VALUE) { // Only descripcion
				query = "SELECT * FROM Aplicacion WHERE Descripcion = '" + descripcion + "'";
			} else if (descripcion.isBlank() && gestor.isBlank() && servidor == Byte.MIN_VALUE) { // Only id
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "'";
			} else if (id.isBlank() && descripcion.isBlank()) { // Only gestor and servidor
				query = "SELECT * FROM Aplicacion WHERE Gestor = '" + gestor + "' AND Servidor = '" + servidor + "'";
			} else if (id.isBlank() && gestor.isBlank()) { // Only descripcion and servidor
				query = "SELECT * FROM Aplicacion WHERE Descripcion = '" + descripcion + "' AND Servidor = '" + servidor
						+ "'";
			} else if (descripcion.isBlank() && gestor.isBlank()) { // Only id and servidor
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Servidor = '" + servidor + "'";
			} else if (id.isBlank() && servidor == Byte.MIN_VALUE) { // Only descripcion and gestor
				query = "SELECT * FROM Aplicacion WHERE Descripcion = '" + descripcion + "' AND Gestor = '" + gestor + "'";
			} else if (descripcion.isBlank() && servidor == Byte.MIN_VALUE) { // Only id and gestor
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Gestor = '" + gestor + "'";
			} else if (gestor.isBlank() && servidor == Byte.MIN_VALUE) { // Only id and descripcion
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Descripcion = '" + descripcion + "'";
			} else if (id.isBlank()) { // descripcion, gestor and servidor
				query = "SELECT * FROM Aplicacion WHERE Descripcion = '" + descripcion + "' AND Gestor = '" + gestor
						+ "' AND Servidor = '" + servidor + "'";
			} else if (descripcion.isBlank()) { // id, gestor and servidor
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Gestor = '" + gestor + "' AND Servidor = '"
						+ servidor + "'";
			} else if (gestor.isBlank()) { // id, descripcion and servidor
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Descripcion = '" + descripcion
						+ "' AND Servidor = '" + servidor + "'";
			} else if (servidor == Byte.MIN_VALUE) { // id, descripcion and gestor
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Descripcion = '" + descripcion
						+ "' AND Gestor = '" + gestor + "'";
			} else { // All fields are not empty
				query = "SELECT * FROM Aplicacion WHERE ID = '" + id + "' AND Descripcion = '" + descripcion
						+ "' AND Gestor = '" + gestor + "' AND Servidor = '" + servidor + "'";
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
	public boolean deleteAplicacion(String id) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "DELETE FROM Aplicacion WHERE ID = '" + id + "'";
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

}
