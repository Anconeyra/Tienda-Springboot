package curso.java.tienda.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import curso.java.tienda.model.Producto;
import curso.java.tienda.service.ProductoService;


@Controller
@RequestMapping("")
public class ProductoController {
	

	@Autowired
	ProductoService ps;
	
	
	@GetMapping("")
	public String inicio(Model model, HttpSession miSesion) {
		ArrayList<Producto> catalogo = ps.obtenerProductos();		
		model.addAttribute("catalogo", catalogo);
		Object carritoSesion = miSesion.getAttribute("carrito"); 
		ArrayList<Producto> carrito;
		
		if(carritoSesion == null) {
			carrito = new ArrayList<Producto>();
		}else {
			carrito = (ArrayList<Producto>) carritoSesion;
		}
		
		miSesion.setAttribute("carrito", carrito);
		return "index";
	}
	
	/**
	 * Metodo que permite agregar un producto al carrito del usuario.
	 * Almaceno el precio del producto en la sesion para mostrar el total del carro.
	 * 
	 * @param miSesion Objeto que contiene la sesion
	 * @param id El id del producto seleccionado
	 * @return devuelve el index
	 */
	@GetMapping("/meterProducto")
	public String meterProducto(HttpSession miSesion, @RequestParam(name="id", required=false) int id) {
		ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
		Producto producto;
		producto = ps.buscarProducto(id);
		carrito.add(producto);
		miSesion.setAttribute("carrito", carrito);
		
		double totalCarrito = 0.0;
		for(Producto productos : carrito) {
			double precio = productos.getPrecio();
			totalCarrito += precio;
		}
		miSesion.setAttribute("totalCarrito", Math.round(totalCarrito*100.00)/100.00);
		
		return "redirect:/";
	}
	
	/**
	 * Metodo que permite buscar un producto mediante un campo de busqueda.
	 * Mostrara los productos que coincidan con dicha busqueda.
	 * 
	 * 
	 * @param model Objeto que contiene el modelo
	 * @param miSesion Objeto que contiene la sesion
	 * @param campoBuscar el texto introducido por el usuario en el campo buscar
	 * @return devuelve el index
	 */
	@GetMapping("/buscarProducto")
	public String buscarProducto(Model model, HttpSession miSesion, @RequestParam(name="buscar", required=false) String campoBuscar) {
		if(campoBuscar != null) {
			ArrayList<Producto> productosBuscados = new ArrayList<Producto>();
			productosBuscados = ps.buscarCampo(campoBuscar);
			miSesion.setAttribute("productosBuscados", productosBuscados);
			model.addAttribute("catalogo", productosBuscados);
			return "index";
		}
		
		return "redirect:/";
	}
	
	/**
	 * Metodo que permite ver el detalle de un producto seleccionado en el index.
	 * 
	 * @param id el id del producto selecionado
	 * @param miSesion Objeto que contiene la sesion
	 * @return devuelve el detalle del producto
	 */
	@GetMapping("/anonimo/detalleProducto/{id}")
	public String detalleProducto(@PathVariable("id") Integer id, HttpSession miSesion) {
		Producto detalleDeProducto = ps.buscarProducto(id);
		miSesion.setAttribute("detalleProducto", detalleDeProducto);
		return "anonimo/detalleProducto";
	}
}
