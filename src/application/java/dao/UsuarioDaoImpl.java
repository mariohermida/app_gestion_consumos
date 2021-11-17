package application.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import application.java.model.Usuario;

/**
 * Class that implements the methods defined in UsuarioDao (DAO design pattern
 * is applied)
 */
public class UsuarioDaoImpl implements UsuarioDao {

	private Connection connection;

	// Database information
	static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_consumos";
	static final String DB_USER = "root";
	static final String DB_PASS = "root";

	@Override
	public List<Usuario> getAllUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "SELECT * FROM usuario";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				usuarios.add(new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}
			connection.close();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuarios;
	}

	@Override
	public List<Usuario> getUsuarios(String id, String nombre, String telefono) {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Depending on the fields given a different query is created
			String query;
			if (id.isBlank() && nombre.isBlank()) { // Only telefono
				query = "SELECT * FROM Usuario WHERE Telefono = '" + telefono + "'";
			} else if (id.isBlank() && telefono.isBlank()) { // Only nombre
				query = "SELECT * FROM Usuario WHERE Nombre = '" + nombre + "'";
			} else if (nombre.isBlank() && telefono.isBlank()) { // Only id
				query = "SELECT * FROM Usuario WHERE ID = '" + id + "'";
			} else if (id.isBlank()) { // nombre and telefono
				query = "SELECT * FROM Usuario WHERE Nombre = '" + nombre + "' AND Telefono = '" + telefono + "'";
			} else if (nombre.isBlank()) { // id and telefono
				query = "SELECT * FROM Usuario WHERE ID = '" + id + "' AND Telefono = '" + telefono + "'";
			} else if (telefono.isBlank()) { // id and nombre
				query = "SELECT * FROM Usuario WHERE ID = '" + id + "' AND Nombre = '" + nombre + "'";
			} else { // All fields are not empty
				query = "SELECT * FROM Usuario WHERE ID = '" + id + "' AND Nombre = '" + nombre + "' AND Telefono = '"
						+ telefono + "'";
			}
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				usuarios.add(new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}
			connection.close();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuarios;
	}

	@Override
	public boolean insertUsuario(Usuario usuario) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "INSERT INTO Usuario (ID, Nombre, Apellidos, Email, Telefono, Direccion) VALUES ('"
					+ usuario.getId() + "', '" + usuario.getNombre() + "', '" + usuario.getApellidos() + "', '"
					+ usuario.getEmail() + "', '" + usuario.getTelefono() + "', '" + usuario.getDireccion() + "')";
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
	public boolean deleteUsuario(String id) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "DELETE FROM Usuario WHERE ID = '" + id + "'";
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
	public boolean updateUsuario(String id, Usuario usuario) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "UPDATE Usuario SET ID = '" + usuario.getId() + "', Nombre = '" + usuario.getNombre()
					+ "', Apellidos = '" + usuario.getApellidos() + "', Email = '" + usuario.getEmail()
					+ "', Telefono = '" + usuario.getTelefono() + "', Direccion = '" + usuario.getDireccion()
					+ "' WHERE ID = '" + id + "'";
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
