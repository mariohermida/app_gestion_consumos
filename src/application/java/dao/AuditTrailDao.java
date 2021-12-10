package application.java.dao;

import java.util.List;
import application.java.model.AuditTrail;

public interface AuditTrailDao {

	public List<AuditTrail> getAllLogs();

}
