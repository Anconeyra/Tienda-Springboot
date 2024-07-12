package curso.java.tienda.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import curso.java.tienda.model.Estado;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	ArrayList<Pedido> findByUsuario(Usuario usuario);
	
	@Transactional
	@Modifying
	@Query("update Pedido p set p.estado_pedido = ?1 where p.id = ?2")
	void cancelarPedido(Estado estado_pedido, int id);
}
