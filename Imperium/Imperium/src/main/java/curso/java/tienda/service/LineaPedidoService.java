package curso.java.tienda.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.LineaPedido;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.repository.LineaPedidoRepository;

@Service
public class LineaPedidoService {
	@Autowired
	LineaPedidoRepository lpr;
	
	public void crearLineaPedido(LineaPedido lineapedido) {
		lpr.save(lineapedido);
	}
	
	public ArrayList<LineaPedido> verLineasPedido(Pedido pedido){
		ArrayList<LineaPedido> lineasPedido = new ArrayList<LineaPedido>();
		lineasPedido = lpr.findByPedido(pedido);
		return lineasPedido;
	}
}
