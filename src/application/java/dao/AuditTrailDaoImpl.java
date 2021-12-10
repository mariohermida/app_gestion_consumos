package application.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import application.java.model.AuditTrail;

/**
 * Class that implements the methods defined in AplicacionDao (DAO design
 * pattern is applied)
 */
public class AuditTrailDaoImpl implements AuditTrailDao {

	private Connection connection;

	// Database information
	static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_consumos";
	// Root permissions are needed to retrieve the logs
	static final String DB_USER = "root";
	static final String DB_PASS = "root";

	@Override
	public List<AuditTrail> getAllAplicaciones() {
		List<AuditTrail> logs = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String query = "SELECT * FROM Log";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				logs.add(new AuditTrail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5)));
			}
			connection.close();
			return logs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logs;
	}

}
