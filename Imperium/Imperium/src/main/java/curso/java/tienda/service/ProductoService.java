package curso.java.tienda.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Producto;
import curso.java.tienda.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	ProductoRepository pr;
	
	public ArrayList<Producto> obtenerProductos() {
		ArrayList<Producto> catalogo = new ArrayList<Producto>();
		catalogo = (ArrayList<Producto>)pr.findAll();
		return catalogo;
	}
	
	public Producto buscarProducto(int id){
		Producto productoDevolver = null;
		Optional<Producto> opcional = pr.findById(id);
		productoDevolver = opcional.get();
		
		return productoDevolver;
	}
	
	public void crearProducto(Producto producto) {
		pr.save(producto);
	}
	
	public void eliminarProducto(Producto producto) {
		pr.delete(producto);
	}
	
	public void editarProducto(Producto producto) {
		pr.save(producto);
	}
	
	public ArrayList<Producto> buscarCampo(String campoBuscar){
		ArrayList<Producto> productos = new ArrayList<Producto>();
		productos = pr.buscarCampo(campoBuscar);
		return productos;
	}
	
	//Metodo para insertar datos.
	public ArrayList<Producto> insertarDatosProductos() {
		ArrayList<Producto> catalogo = new ArrayList<Producto>();
		catalogo.add(new Producto(1,"Crema de cacahuete","Crema de cacahuete para untar",2.5,1));
		catalogo.add(new Producto(2,"Cacahuetes","Cacahuetes pelados",4.42,1));
		catalogo.add(new Producto(3,"Tortitas de arroz","Tortitas de arroz redondas",1.79,1));
		catalogo.add(new Producto(4,"Tortitas de maiz","Tortitas de maiz redondas",1.69,1));
		catalogo.add(new Producto(5,"Tortitas multigrano","Tortitas multigrano redondas",1.99,1));
		catalogo.add(new Producto(6,"Pan de hamburgesa","Pan de hamburgesa brioche",3.5,1));
		pr.saveAll(catalogo);
		return catalogo;
	}
}
