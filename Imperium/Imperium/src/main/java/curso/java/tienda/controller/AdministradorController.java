package curso.java.tienda.controller;

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
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Roles;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.LineaPedidoService;
import curso.java.tienda.service.PedidoService;
import curso.java.tienda.service.ProductoService;
import curso.java.tienda.service.UsuarioService;
import curso.java.tienda.util.Encriptacion;
import curso.java.tienda.util.PDFFactura;

@Controller
@RequestMapping("/admin")
public class AdministradorController {
	
	@Autowired
	UsuarioService us;
	
	@Autowired
	ProductoService ps;
	
	@Autowired
	PedidoService pes;
	
	@Autowired
	LineaPedidoService lps;
	
	@GetMapping("")
	public String cargaAdministracion(HttpSession miSesion){
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			String nombreAdministrador = miSesion.getAttribute("usuarioAdmin").toString();
			Usuario usuarioAdministrador = us.buscarUsuario(nombreAdministrador);
			miSesion.setAttribute("datosAdministrador", usuarioAdministrador);
			return "administrador/administracionPerfil";
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/login")
	public String cargaLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "administrador/login";
	}
	
	@PostMapping("/login")
	public String loginAdmin(@ModelAttribute Usuario usuario, HttpSession miSesion) {
		if(usuario.getNombre() != null && usuario.getPassword() != null) {
			for(Usuario usuarioBuscado : us.getUsuarios()) {
				if(usuarioBuscado.getNombre().equals(usuario.getNombre()) && Encriptacion.validarPassword(usuario.getPassword(), usuarioBuscado.getPassword()) == true && (usuarioBuscado.getRol().equals(Roles.administrador) || usuarioBuscado.getRol().equals(Roles.empleado))) {
						miSesion.setAttribute("usuarioAdmin", usuario.getNombre());
						if(usuarioBuscado.getRol().equals(Roles.empleado)) {
							miSesion.setAttribute("rolUsuario", Roles.empleado.toString());
						}
						if(usuarioBuscado.getRol().equals(Roles.administrador)) {
							miSesion.setAttribute("rolUsuario", Roles.administrador.toString());
						}
						return "redirect:/admin";
				}
			}
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/cerrarSesion")
	public String cerrarSesion(HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			miSesion.setAttribute("usuarioAdmin", null);
			miSesion.setAttribute("rolUsuario", null);
			return "redirect:/admin/login";
		}
		
		return "redirect:/admin";
	}
	
	@GetMapping("/productos")
	public String cargaProductos(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			ArrayList<Producto> productos = new ArrayList<Producto>();
			productos = ps.obtenerProductos();
			miSesion.setAttribute("productosAdmin", productos);
			return "administrador/administracionProductos";
		}
	
		return "redirect:/admin/login";
	}
	
	@GetMapping("/productos/nuevoProducto")
	public String botonNuevoProducto(Model model) {
		model.addAttribute("producto", new Producto());
		return "administrador/nuevoProducto";
	}
	
	@PostMapping("/productos/crearNuevoProducto")
	public String nuevoProducto(Model model, @ModelAttribute Producto producto, HttpSession miSesion) {
		if(producto.getNombre() != null && producto.getDescripcion() != null && producto.getCantidad() != 0 && producto.getPrecio() != 0) {
			ps.crearProducto(producto);
			return "redirect:/admin/productos";
		}
		
		return "redirect:/admin/productos/crearNuevoProducto";
	}
	
	@GetMapping("/productos/editarProducto/{id}")
	public String editarProducto(Model model, @PathVariable(name="id", required=false) int id) {
		Producto producto = ps.buscarProducto(id);
		model.addAttribute("producto", producto);
		return "administrador/editarProducto";
	}
	
	@PostMapping("/productos/editarProductoExistente")
	public String editarProductoExistente(HttpSession miSesion, @ModelAttribute Producto producto) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			if(producto != null) {
				ps.editarProducto(producto);
				return "redirect:/admin/productos";
			}
		}
		
		return "administrador/editarProducto";
	}
	
	@GetMapping("/productos/eliminarProducto/{id}")
	public String eliminarProducto(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Producto producto = ps.buscarProducto(id);
			ps.eliminarProducto(producto);
			return "redirect:/admin/productos";
		}
		
		return "administrador/administracionProductos";
	}
	
	@GetMapping("/clientes")
	public String cargaClientes(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			ArrayList<Usuario> clientes = new ArrayList<Usuario>();
			Roles rol = Roles.cliente;
			clientes = us.getUsuariosRol(rol);
			miSesion.setAttribute("clientesAdmin", clientes);
			return "administrador/administracionClientes";
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/clientes/nuevoCliente")
	public String botonNuevoCliente(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "administrador/nuevoCliente";
	}
	
	@PostMapping("/clientes/crearNuevoCliente")
	public String nuevoCliente(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/nuevoCliente";
		}else {
			if(usuario.getNombre() != null && usuario.getPassword()  != null && usuario.getNombreUsuario() != null && usuario.getApellido() != null && usuario.getEmail() != null && usuario.getDireccion() != null && usuario.getLocalidad() != null && usuario.getProvincia() != null && usuario.getPais() != null && usuario.getTelefono() != null && usuario.getRol() != null) {
				String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
				Usuario usuarioNuevo = new Usuario(usuario.getNombre(), usuario.getEmail(), passwordEncriptada, usuario.getNombreUsuario(), usuario.getApellido(),  usuario.getDireccion(), usuario.getProvincia(), usuario.getLocalidad(), usuario.getPais(), usuario.getTelefono(), usuario.getRol());
				us.crearUsuario(usuarioNuevo);
				return "redirect:/admin/clientes";
			}
			
			return "redirect:/admin/clientes/crearNuevoCliente";
		}
	}
	
	@GetMapping("/clientes/editarCliente/{id}")
	public String editarCliente(Model model, @PathVariable(name="id", required=false) int id) {
		Usuario usuario = us.getUsuario(id);
		model.addAttribute("usuario", usuario);
		return "administrador/editarCliente";
	}
	
	@PostMapping("/clientes/editarClienteExistente")
	public String editarClienteExistente(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/editarCliente";
		}else {
			if(miSesion.getAttribute("usuarioAdmin") != null) {
				if(usuario != null) {
					us.editarUsuario(usuario);
					return "redirect:/admin/clientes";
				}
			}
			
			return "administrador/editarCliente";
		}
	}
	
	@GetMapping("/clientes/eliminarCliente/{id}")
	public String eliminarCliente(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Usuario empleado = us.getUsuario(id);
			us.eliminarUsuario(empleado);
			return "redirect:/admin/clientes";
		}
		
		return "administrador/administracionClientes";
	}
	
	@GetMapping("/empleados")
	public String cargaEmpleados(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			ArrayList<Usuario> empleados = new ArrayList<Usuario>();
			Roles rol = Roles.empleado;
			empleados = us.getUsuariosRol(rol);
			miSesion.setAttribute("empleadosAdmin", empleados);
			return "administrador/administracionEmpleados";
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/empleados/nuevoEmpleado")
	public String botonNuevoEmpleado(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "administrador/nuevoEmpleado";
	}
	
	@PostMapping("/empleados/crearNuevoEmpleado")
	public String nuevoEmpleado(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/nuevoEmpleado";
		}else {
			if(usuario.getNombre() != null && usuario.getNombre() != null && usuario.getNombreUsuario() != null && usuario.getApellido() != null && usuario.getEmail() != null && usuario.getDireccion() != null && usuario.getLocalidad() != null && usuario.getProvincia() != null && usuario.getPais() != null && usuario.getTelefono() != null && usuario.getRol() != null) {
				String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
				Usuario empleadoNuevo = new Usuario(usuario.getNombre(), usuario.getEmail(), passwordEncriptada, usuario.getNombreUsuario(), usuario.getApellido(),  usuario.getDireccion(), usuario.getProvincia(), usuario.getLocalidad(), usuario.getPais(), usuario.getTelefono(), usuario.getRol());
				us.crearUsuario(empleadoNuevo);
				return "redirect:/admin/empleados";
			}
		
			return "redirect:/admin/empleados/crearNuevoEmpleado";
		}
	}
	
	@GetMapping("/empleados/editarEmpleado/{id}")
	public String editarEmpleado(Model model, @PathVariable(name="id", required=false) int id) {
		Usuario usuario = us.getUsuario(id);
		model.addAttribute("usuario", usuario);
		return "administrador/editarEmpleado";
	}
	
	@PostMapping("/empleados/editarEmpleadoExistente")
	public String editarEmpleadoExistente(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/editarEmpleado";
		}else {
			if(miSesion.getAttribute("usuarioAdmin") != null) {
				if(usuario != null) {
					us.editarUsuario(usuario);
					return "redirect:/admin/empleados";
				}
			}
			
			return "administrador/editarEmpleado";
		}
	}
	
	@GetMapping("/empleados/eliminarEmpleado/{id}")
	public String eliminarEmpleado(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Usuario empleado = us.getUsuario(id);
			us.eliminarUsuario(empleado);
			return "redirect:/admin/empleados";
		}
		
		return "administrador/administracionEmpleados";
	}
	
	
	
	
	
	
	
	@GetMapping("/administradores")
	public String cargaAdministradores(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			ArrayList<Usuario> administradores = new ArrayList<Usuario>();
			Roles rol = Roles.administrador;
			administradores = us.getUsuariosRol(rol);
			miSesion.setAttribute("administradoresAdmin", administradores);
			return "administrador/administracionAdministradores";
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/administradores/nuevoAdministrador")
	public String botonNuevoAdministrador(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "administrador/nuevoAdministrador";
	}
	
	@PostMapping("/administradores/crearNuevoAdministrador")
	public String nuevoAdministrador(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/nuevoAdministrador";
		}else {
			if(usuario.getNombre() != null && usuario.getNombre() != null && usuario.getNombreUsuario() != null && usuario.getApellido() != null && usuario.getEmail() != null && usuario.getDireccion() != null && usuario.getLocalidad() != null && usuario.getProvincia() != null && usuario.getPais() != null && usuario.getTelefono() != null && usuario.getRol() != null) {
				String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
				Usuario administradorNuevo = new Usuario(usuario.getNombre(), usuario.getEmail(), passwordEncriptada, usuario.getNombreUsuario(), usuario.getApellido(),  usuario.getDireccion(), usuario.getProvincia(), usuario.getLocalidad(), usuario.getPais(), usuario.getTelefono(), usuario.getRol());
				us.crearUsuario(administradorNuevo);
				return "redirect:/admin/administradores";
			}
		
			return "redirect:/admin/administradores/crearNuevoAdministrador";
		}
	}
	
	@GetMapping("/administradores/editarAdministrador/{id}")
	public String editarAdministrador(Model model, @PathVariable(name="id", required=false) int id) {
		Usuario usuario = us.getUsuario(id);
		model.addAttribute("usuario", usuario);
		return "administrador/editarAdministrador";
	}
	
	@PostMapping("/administradores/editarAdministradorExistente")
	public String editarAdministradorExistente(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion) {
		if(br.hasErrors()) {
			return "administrador/editarAdministrador";
		}else {
			if(miSesion.getAttribute("usuarioAdmin") != null) {
				if(usuario != null) {
					us.editarUsuario(usuario);
					return "redirect:/admin/administradores";
				}
			}
			
			return "administrador/editarAdministrador";
		}
	}
	
	@GetMapping("/administradores/eliminarAdministrador/{id}")
	public String eliminarAdministrador(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Usuario administrador = us.getUsuario(id);
			us.eliminarUsuario(administrador);
			return "redirect:/admin/administradores";
		}
		
		return "administrador/administracionAdministradores";
	}
	
	
	
	
	
	
	
	
	@GetMapping("/procesarPedidos")
	public String cargaPedidos(Model model, HttpSession miSesion) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
			pedidos = pes.getPedidos();
			miSesion.setAttribute("pedidosAdmin", pedidos);
			return "administrador/administracionProcesarPedidos";
		}
		
		return "redirect:/admin/login";
	}
	
	@GetMapping("/procesarPedidos/enviarPedido/{id}")
	public String enviarPedido(RedirectAttributes ra, HttpSession miSesion, @PathVariable(name="id") int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Pedido pedidoSelecionado = pes.obtenerPedidoUsuario(id);
			if(pedidoSelecionado.getEstado_pedido() != Estado.PC && pedidoSelecionado.getEstado_pedido() != Estado.C && pedidoSelecionado.getEstado_pedido() != Estado.E) {
				Estado nuevoEstado = Estado.E;
				pes.cambiarEstado(nuevoEstado, id);
				
				Usuario usuario = us.buscarUsuario(miSesion.getAttribute("usuarioAdmin").toString());
				Pedido pedido = pes.obtenerPedidoUsuario(id);
				ArrayList<LineaPedido> productosPedido = new ArrayList<LineaPedido>();
				productosPedido = lps.verLineasPedido(pedido);
				PDFFactura.crearFactura(usuario, pedido, productosPedido);
				return "redirect:/admin/procesarPedidos";
			}else {
				String numPedido = pedidoSelecionado.getNum_factura();
				if(pedidoSelecionado.getEstado_pedido() == Estado.PC) {
					ra.addFlashAttribute("errorEnviarPedidoAdmin", "No se puede enviar el pedido " + numPedido + " por que esta pendiente de cancelacion.");
				}
				if(pedidoSelecionado.getEstado_pedido() == Estado.C) {
					ra.addFlashAttribute("errorEnviarPedidoAdmin", "No se puede enviar el pedido " + numPedido + " por que ha sido cancelado.");
				}
				if(pedidoSelecionado.getEstado_pedido() == Estado.E) {
					ra.addFlashAttribute("errorEnviarPedidoAdmin", "No se puede enviar el pedido " + numPedido + " por que ya ha sido enviado.");
				}
				return "redirect:/admin/procesarPedidos";
			}
		}
		return "redirect:/admin";
	}
	
	@GetMapping("/procesarPedidos/cancelarPedido/{id}")
	public String cancelarPedido(RedirectAttributes ra, HttpSession miSesion, @PathVariable(name="id") int id) {
		if(miSesion.getAttribute("usuarioAdmin") != null) {
			Pedido pedidoSelecionado = pes.obtenerPedidoUsuario(id);
			if(pedidoSelecionado.getEstado_pedido() != Estado.E && pedidoSelecionado.getEstado_pedido() != Estado.C) {
				Estado nuevoEstado = Estado.C;
				pes.cambiarEstado(nuevoEstado, id);
				return "redirect:/admin/procesarPedidos";
			}else {
				String numPedido = pedidoSelecionado.getNum_factura();
				if(pedidoSelecionado.getEstado_pedido() == Estado.E) {
					ra.addFlashAttribute("errorCancelarPedidoAdmin", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido enviado.");
				}
				if(pedidoSelecionado.getEstado_pedido() == Estado.C) {
					ra.addFlashAttribute("errorCancelarPedidoAdmin", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido cancelado.");
				}
				return "redirect:/admin/procesarPedidos";
			}
		}
		return "redirect:/admin";
	}
}
