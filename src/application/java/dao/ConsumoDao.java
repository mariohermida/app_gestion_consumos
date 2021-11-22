package application.java.dao;

import java.util.List;

import application.java.model.Aplicacion;
import application.java.model.Consumo;
import application.java.model.Mes;
import application.java.model.Usuario;

public interface ConsumoDao {

	public List<Consumo> getAllConsumos();

	public List<Consumo> getConsumos(int id, Usuario usuario, Aplicacion aplicacion, Mes mes, int consumo);

	public boolean insertConsumo(Consumo consumo);

	public boolean deleteConsumos(int id, Usuario usuario, Aplicacion aplicacion, Mes mes, int consumo);

	public boolean updateConsumo(int id, Consumo consumo);
}
