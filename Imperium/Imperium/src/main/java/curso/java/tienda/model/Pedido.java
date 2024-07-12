package curso.java.tienda.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@ManyToOne
	Usuario usuario;
	@Column(name = "fecha")
	private Timestamp fecha;
	@Column(name = "metodo_pago")
	@Enumerated(EnumType.STRING)
	private MetodoPago metodo_pago;
	@Column(name = "estado_pedido")
	@Enumerated(EnumType.STRING)
	private Estado estado_pedido;
	@Column(name = "num_factura")
	private String num_factura;
	@Column(name = "total")
	private double total;
	
	public Pedido(Estado estado_pedido, int id) {
		this.estado_pedido = estado_pedido;
		this.id = id;
	}
	
	public Pedido(Usuario usuario, Timestamp fecha, MetodoPago metodo_pago, Estado estado_pedido, String num_factura, double total) {
		this.usuario = usuario;
		this.fecha = fecha;
		this.metodo_pago = metodo_pago;
		this.estado_pedido = estado_pedido;
		this.num_factura = num_factura;
		this.total = total;
	}
}
