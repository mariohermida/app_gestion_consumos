package application.java.dao;

import java.util.List;
import application.java.model.Usuario_interno;

public interface Usuario_internoDao {

	public List<Usuario_interno> getAllUsuarios();

	public List<Usuario_interno> getUsuarios(String user, byte admin);

	public boolean updateUsuario(String user, Usuario_interno usuario);

	public boolean insertUsuario(Usuario_interno usuario);

	public boolean deleteUsuarios(String user);
}
