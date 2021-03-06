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

import application.java.model.Usuario;

/**
 * Class that implements the methods defined in UsuarioDao (DAO design pattern
 * is applied)
 */
public class UsuarioDaoImpl implements UsuarioDao {

	private Connection connection;

	// Database credentials
	final String DB_URL;
	final String DB_USER;
	final String DB_PASS;

	public UsuarioDaoImpl() {
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
	public List<Usuario> getAllUsuarios() {
		return getUsuarios("", "", "", "", "", "");
	}

	@Override
	public List<Usuario> getUsuarios(String id, String nombre, String apellidos, String email, String telefono,
			String direccion) {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing usuarios
			String query = "SELECT * FROM Usuario WHERE 1 = 1";
			if (!id.isBlank()) {
				query += " AND ID LIKE '%" + id + "%'";
			}
			if (!nombre.isBlank()) {
				query += " AND Nombre LIKE '%" + nombre + "%'";
			}
			if (!apellidos.isBlank()) {
				query += " AND Apellidos LIKE '%" + apellidos + "%'";
			}
			if (!email.isBlank()) {
				query += " AND Email LIKE '%" + email + "%'";
			}
			if (!telefono.isBlank()) {
				query += " AND Telefono LIKE '%" + telefono + "%'";
			}
			if (!direccion.isBlank()) {
				query += " AND Direccion LIKE '%" + direccion + "%'";
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
	public boolean deleteUsuarios(String id, String nombre, String apellidos, String email, String telefono,
			String direccion) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "DELETE FROM Usuario WHERE 1 = 1";
			if (!id.isBlank()) {
				// ID must be taken as exact input, otherwise IDs containing id string will be
				// deleted
				query += " AND ID = '" + id + "'";
			}
			if (!nombre.isBlank()) {
				query += " AND Nombre LIKE '%" + nombre + "%'";
			}
			if (!apellidos.isBlank()) {
				query += " AND Apellidos LIKE '%" + apellidos + "%'";
			}
			if (!email.isBlank()) {
				query += " AND Email LIKE '%" + email + "%'";
			}
			if (!telefono.isBlank()) {
				query += " AND Telefono LIKE '%" + telefono + "%'";
			}
			if (!direccion.isBlank()) {
				query += " AND Direccion LIKE '%" + direccion + "%'";
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
