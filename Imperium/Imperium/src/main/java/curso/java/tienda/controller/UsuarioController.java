package curso.java.tienda.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import curso.java.tienda.model.Estado;
import curso.java.tienda.model.LineaPedido;
import curso.java.tienda.model.MetodoPago;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Roles;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.LineaPedidoService;
import curso.java.tienda.service.PedidoService;
import curso.java.tienda.service.ProductoService;
import curso.java.tienda.service.UsuarioService;
import curso.java.tienda.util.Encriptacion;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService us;
	
	@Autowired
	private ProductoService ps;
	
	@Autowired
	private PedidoService pes;
	
	@Autowired
	private LineaPedidoService lps;
	
	@GetMapping("/login")
	public String cargaLoginUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "usuario/login";
	}
	
	@PostMapping("/login")
	public String loginUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion, Model model, RedirectAttributes ra) {
		if(br.hasErrors()) {
			model.addAttribute("usuario", usuario);
			return "usuario/login";
		}
		if(usuario.getNombre() != null && usuario.getPassword() != null) {
			for(Usuario usuarioBuscado : us.getUsuarios()) {
				if(usuarioBuscado.getNombre().equals(usuario.getNombre()) && Encriptacion.validarPassword(usuario.getPassword(), usuarioBuscado.getPassword()) == true) {
						miSesion.setAttribute("nombre", usuario.getNombre());
						ps.obtenerProductos();
						return "redirect:/";
				}
			}
		}
		
		ra.addFlashAttribute("errorUsuarioNoExiste", "Usuario o contraseña incorrectos.");
		return "redirect:/usuario/login";
	}
	
	@GetMapping("/registro")
	public String cargaRegistroUsuario(Model model){
		model.addAttribute("usuario", new Usuario());
		return "usuario/registro";
	}
	
	@PostMapping("/registro")
	public String registroUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, RedirectAttributes ra, @RequestParam(name="repitPassword", required=false) String passwordRepetida, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "usuario/registro";
		}else {
			Usuario usuarioExiste = us.buscarUsuario(usuario.getNombre());
			if(usuarioExiste == null) {
				if(usuario.getNombre() != null && usuario.getPassword() != null && passwordRepetida != null) {
					Roles rol = Roles.cliente;
					usuario.setRol(rol);
					String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
					usuario.setPassword(passwordEncriptada);
					us.crearUsuario(usuario);
					miSesion.setAttribute("nombre", usuario.getNombre());
					return "redirect:/";
				}
			}
			ra.addFlashAttribute("errorUsuarioExiste", "El nombre de usuario ya existe.");
			return "redirect:/usuario/registro";
		}
	}
	
	@GetMapping("/miPerfil")
	public String cargaMiPerfil(HttpSession miSesion) {
		if(miSesion.getAttribute("nombre") != null) {
			String nombreUsuario = miSesion.getAttribute("nombre").toString();
			Usuario usuario = us.buscarUsuario(nombreUsuario);
			miSesion.setAttribute("datosUsuario", usuario);
			return "usuario/miPerfil";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/miPerfil/editarPerfil")
	public String cargaEditarPerfil(Model model, HttpSession miSesion) {
		Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
		model.addAttribute(usuario);
		return "usuario/editarPerfil";
	}
	
	
	@PostMapping("/miPerfil/editarPerfilCliente")
	public String editarPerfilCliente(HttpSession miSesion, @ModelAttribute Usuario usuario) {
		if(miSesion.getAttribute("nombre") != null) {
			if(usuario != null) {
				us.editarUsuario(usuario);
				miSesion.setAttribute("datosUsuario", usuario);
				return "redirect:/usuario/miPerfil";
			}
		}
		
		return "usuario/miPerfil/editarPerfil";
	}
	
	
	@GetMapping("/miPerfil/cerrarSesion")
	public String cerrarSesion(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("nombre") != null) {
			miSesion.setAttribute("nombre", null);
			return "redirect:/";
		}
		
		return "usuario/miPerfil";
	}
	
	@GetMapping("/miPerfil/misPedidos")
	public String cargaMisPedidos(HttpSession miSesion) {
		if(miSesion.getAttribute("nombre") != null) {
			ArrayList<Pedido> pedidosUsuario = new ArrayList<Pedido>();
			Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
			pedidosUsuario = pes.obtenerPedidosUsuario(usuario);
			miSesion.setAttribute("pedidosUsuario", pedidosUsuario);
			return "usuario/miPedidos";
		}
		return "redirect:/";
	}
	
	@GetMapping("/miPerfil/cancelarPedido/{id}")
	public String cancelarPedido(RedirectAttributes ra, HttpSession miSesion, @PathVariable(name="id") int id) {
		if(miSesion.getAttribute("nombre") != null) {
			Pedido pedidoSelecionado = pes.obtenerPedidoUsuario(id);
			if(pedidoSelecionado.getEstado_pedido() != Estado.E && pedidoSelecionado.getEstado_pedido() != Estado.C && pedidoSelecionado.getEstado_pedido() != Estado.PC) {
				Estado nuevoEstado = Estado.PC;
				pes.cambiarEstado(nuevoEstado, id);
				return "redirect:/usuario/miPerfil/misPedidos";
			}else {
				String numPedido = pedidoSelecionado.getNum_factura();
				if(pedidoSelecionado.getEstado_pedido() == Estado.E) {
					ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido enviado.");
				}
				if(pedidoSelecionado.getEstado_pedido() == Estado.C) {
					ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido cancelado.");
				}
				if(pedidoSelecionado.getEstado_pedido() == Estado.PC) {
					ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya esta pendiente de cancelacion.");
				}
			}
		}
		return "redirect:/usuario/miPerfil/misPedidos";
	}
	
	@GetMapping("/miPerfil/verDetallePedido/{id}")
	public String detallePedido(HttpSession miSesion, @PathVariable(name="id") int id) {
		if(miSesion.getAttribute("nombre") != null) {
			Pedido pedido = pes.obtenerPedidoUsuario(id);
			ArrayList<LineaPedido> detallePedido = new ArrayList<LineaPedido>();
			detallePedido = lps.verLineasPedido(pedido);
			double sinImpuestos = 0.0;
			double iva = 0.0;
			for(LineaPedido detalle : detallePedido) {
				 sinImpuestos += detalle.getTotal();
				 iva += detalle.getTotal()*0.04;
			}
			miSesion.setAttribute("detallePedido", detallePedido);
			miSesion.setAttribute("datosPedido", pedido);
			miSesion.setAttribute("subtotalSinImpuestos", Math.round(sinImpuestos*100.00)/100.00);
			miSesion.setAttribute("iva", Math.round(iva*100.00)/100.00);
			return "usuario/miDetalle";
		}
		return "redirect:/";
	}
	
	@GetMapping("/carrito")
	public String cargaCarrito(HttpSession miSesion) {
		if(miSesion.getAttribute("carrito") != null) {
			return "usuario/carrito";
		}
		return "redirect:/";
	}
	
	@GetMapping("/carrito/eliminarProducto")
	public String eliminarProducto(HttpSession miSesion, @RequestParam(name="id", required=false) int id) {
		ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
		Producto producto = ps.buscarProducto(id);
		carrito.remove(producto);
		miSesion.setAttribute("carrito", carrito);
		double totalAnterior = (double)miSesion.getAttribute("totalCarrito");
		double nuevoTotal = totalAnterior - producto.getPrecio();
		miSesion.setAttribute("totalCarrito", nuevoTotal);
		
		return "redirect:/usuario/carrito";
	}
	
	@GetMapping("/carrito/finalizarCompra")
	public String finalizarCompra(HttpSession miSesion) {
		if(miSesion.getAttribute("nombre") != null) {
			if(miSesion.getAttribute("carrito") != null) {
				ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
				int productosTotales = carrito.size();
				double precioSinImpuestos = 0.0;
				double precioTotal = 0.0;
				double impuestos = 0.0;
				for(Producto producto : carrito) {
					double precio = producto.getPrecio();
					precioSinImpuestos += precio;
					double precioImpuestos = precio * 0.04;
					precio += precioImpuestos;
					impuestos += precioImpuestos;
					precioTotal += precio;
				}
				miSesion.setAttribute("productosTotales", productosTotales);
				Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
				String email = usuario.getEmail();
				miSesion.setAttribute("email", email);
				miSesion.setAttribute("impuestos", Math.round(impuestos*100.00)/100.00);
				miSesion.setAttribute("precioSinImpuestos", Math.round(precioSinImpuestos*100.00)/100.00);
				miSesion.setAttribute("precioTotal", Math.round(precioTotal*100.00)/100.00);
				return "redirect:/usuario/pago";
			}else {
				return "redirect:/";
			}
			
		}
		
		return "redirect:/usuario/login";
	}
	
	@GetMapping("/pago")
	public String cargaPago(Model model){
		model.addAttribute("usuarioPago", new Usuario());
		return "usuario/pago";
	}
	
	@PostMapping("/pago")
	public String pagarCompra(RedirectAttributes ra, HttpSession miSesion, @RequestParam(name = "paymentMethod") String metodoDePago){
		if(miSesion.getAttribute("nombre") != null) {
			if(miSesion.getAttribute("carrito") != null) {
				ArrayList<Producto> contenidoCarrito = (ArrayList<Producto>)miSesion.getAttribute("carrito");
				Estado estado = Estado.PE;
				Long datetime = System.currentTimeMillis();
				Timestamp fechaActual = new Timestamp(datetime);
				
				Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
				String numFactura = String.valueOf(usuario.getId()) + datetime.toString();
				Double total = 0.0;
				for(Producto producto: contenidoCarrito) {
					double precio = producto.getPrecio();
					double precioImpuestos = precio * 0.04;
					precio += precioImpuestos;
					total += precio;
				}
				Pedido pedidoCreado = new Pedido(usuario ,fechaActual, MetodoPago.valueOf(metodoDePago), estado, numFactura, Math.round(total*100.00)/100.00);
				pes.crearPedido(pedidoCreado);
				for(Producto producto : contenidoCarrito) {
					double impuestod = producto.getPrecio()*0.04;
					float impuesto = (float)impuestod;
					float precioUnidad = (float)producto.getPrecio();
					LineaPedido lineaPedido = new LineaPedido(pedidoCreado, producto, precioUnidad, producto.getCantidad(), impuesto, producto.getPrecio());
					lps.crearLineaPedido(lineaPedido);
				}
				miSesion.setAttribute("pedido", pedidoCreado);
				miSesion.setAttribute("fechaActual", new SimpleDateFormat("dd/MM/yyyy").format(fechaActual));
				
				ArrayList<Producto> detalleProductos = (ArrayList<Producto>)miSesion.getAttribute("carrito");
				ra.addFlashAttribute("detalleProductos", detalleProductos.clone());
				miSesion.setAttribute("carrito", null);
				return "redirect:/usuario/confirmacionPedido";
			}
			return "redirect:/";
		}
		
		return "redirect:/usuario/login";
	}
	
	@GetMapping("/confirmacionPedido")
	public String cargaConfirmacion(Model model) {
		model.addAttribute("detalleProductos", model.getAttribute("detalleProductos"));
		return "usuario/confirmacionPedido";
	}
	
	@GetMapping("/confirmacionPedido/verPedidos")
	public String confirmarPedidoVerPedidos(HttpSession miSesion) {
		return "redirect:/usuario/miPerfil/misPedidos";
	}
	
	@GetMapping("/confirmacionPedido/volverInicio")
	public String confirmarPedidoVolverInicio(HttpSession miSesion) {
		return "redirect:/";
	}
}