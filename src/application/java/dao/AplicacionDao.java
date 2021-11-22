package application.java.dao;

import java.util.List;
import application.java.model.Aplicacion;

public interface AplicacionDao {

	public List<Aplicacion> getAllAplicaciones();

	public List<Aplicacion> getAplicaciones(String id, String descripcion, String gestor, byte servidor);

	public boolean insertAplicacion(Aplicacion aplicacion);

	public boolean deleteAplicaciones(String id, String descripcion, String gestor, byte servidor);

	public boolean updateAplicacion(String id, Aplicacion aplicacion);
}
