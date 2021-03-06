package application.java.dao;

import java.util.List;
import application.java.model.Usuario_interno;

public interface Usuario_internoDao {

	public List<Usuario_interno> getAllUsuarios();
	
	public Usuario_interno getUsuario(String user);

	public List<Usuario_interno> getUsuarios(String user, byte permiso);

	public boolean updateUsuario(String user, Usuario_interno usuario, boolean password);

	public boolean insertUsuario(Usuario_interno usuario);
	
	public boolean deleteAllUsuarios();

	public boolean deleteUsuarios(String user);
}
