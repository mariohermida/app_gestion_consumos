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

import application.java.model.AuditTrail;

/**
 * Class that implements the methods defined in AplicacionDao (DAO design
 * pattern is applied)
 */
public class AuditTrailDaoImpl implements AuditTrailDao {

	private Connection connection;

	// Database credentials
	final String DB_URL;
	final String DB_USER;
	final String DB_PASS;

	public AuditTrailDaoImpl() {
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
	public List<AuditTrail> getAllLogs() {
		List<AuditTrail> logs = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "SELECT * FROM Log";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				logs.add(new AuditTrail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}
			connection.close();
			return logs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logs;
	}

}
