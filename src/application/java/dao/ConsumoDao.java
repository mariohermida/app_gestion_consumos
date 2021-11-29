package application.java.dao;

import java.util.List;
import application.java.model.Consumo;

public interface ConsumoDao {

	public List<Consumo> getAllConsumos();

	public List<Consumo> getConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumoMin, int consumoMax);
	
	public boolean updateConsumo(int id, Consumo consumo);

	public boolean insertConsumo(Consumo consumo);

	public boolean deleteConsumos(int id, String idUsuario, String idAplicacion, byte mes, int consumoMin, int consumoMax);
}
