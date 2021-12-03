package application.java.dao;

import java.util.List;
import application.java.model.Consumo;

public interface ConsumoDao {

	public List<Consumo> getAllConsumos();

	public List<Consumo> getConsumos(String idUsuario, String idAplicacion, String mes, int consumoMin, int consumoMax);

	public boolean updateConsumo(String oldIdUsuario, String oldIdAplicacion, String oldMes, Consumo consumo);

	public boolean insertConsumo(Consumo consumo);

	public boolean deleteConsumos(String idUsuario, String idAplicacion, String mes, int consumoMin, int consumoMax);
}
