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

import application.java.model.Usuario_interno;

/**
 * Class that implements the methods defined in Usuario_internoDao (DAO design
 * pattern is applied)
 */
public class Usuario_internoDaoImpl implements Usuario_internoDao {

	private Connection connection;

	// Database credentials
	final String DB_URL;
	final String DB_USER;
	final String DB_PASS;

	public Usuario_internoDaoImpl() {
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
	public List<Usuario_interno> getAllUsuarios() {
		return getUsuarios("", Byte.MIN_VALUE);
	}

	@Override
	public Usuario_interno getUsuario(String user) {
		Usuario_interno usuario = null;
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing usuarios
			String query = "SELECT * FROM Usuario_interno WHERE user = '" + user + "'";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				usuario = new Usuario_interno(rs.getString(1), rs.getString(2), Byte.valueOf(rs.getString(3)));
			}
			connection.close();
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuario;
	}

	@Override
	public List<Usuario_interno> getUsuarios(String user, byte admin) {
		List<Usuario_interno> usuarios = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			// Dynamic querying
			// By default it shows all existing usuarios
			String query = "SELECT * FROM Usuario_interno WHERE 1 = 1";
			if (!user.isBlank()) {
				query += " AND USER LIKE '%" + user + "%'";
			}
			if (admin != Byte.MIN_VALUE) {
				query += " AND ADMIN LIKE '%" + admin + "%'";
			}
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				usuarios.add(new Usuario_interno(rs.getString(1), rs.getString(2), Byte.valueOf(rs.getString(3))));
			}
			connection.close();
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuarios;
	}

	@Override
	public boolean updateUsuario(String user, Usuario_interno usuario) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertUsuario(Usuario_interno usuario) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUsuarios(String user) {
		// TODO Auto-generated method stub
		return false;
	}

}
