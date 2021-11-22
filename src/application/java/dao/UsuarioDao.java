package application.java.dao;

import java.util.List;
import application.java.model.Usuario;

public interface UsuarioDao {

	public List<Usuario> getAllUsuarios();

	public List<Usuario> getUsuarios(String id, String nombre, String apellidos, String email, String telefono,
			String direccion);

	public boolean insertUsuario(Usuario usuario);

	public boolean deleteUsuarios(String id, String nombre, String apellidos, String email, String telefono,
			String direccion);

	public boolean updateUsuario(String id, Usuario usuario);

}
