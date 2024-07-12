package curso.java.tienda.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "detalles_pedido")
public class LineaPedido {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@ManyToOne
	Pedido pedido;
	@ManyToOne
	Producto producto;
	@Column(name = "precio_unidad")
	private float precio_unidad;
	@Column(name = "unidades")
	private int unidades;
	@Column(name = "impuesto")
	private float impuesto;
	@Column(name = "total")
	private double total;
	public LineaPedido(Pedido pedido, Producto producto, float precio_unidad, int unidades, float impuesto, double total) {
		this.pedido = pedido;
		this.producto = producto;
		this.precio_unidad = precio_unidad;
		this.unidades = unidades;
		this.impuesto = impuesto;
		this.total = total;
	}
}
