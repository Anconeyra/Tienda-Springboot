package curso.java.tienda.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import curso.java.tienda.model.LineaPedido;
import curso.java.tienda.model.Pedido;

public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer>{
	ArrayList<LineaPedido> findByPedido(Pedido pedido);
}
