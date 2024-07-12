package curso.java.tienda.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import curso.java.tienda.model.Estado;
import curso.java.tienda.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	@Query("select p from Producto p where p.nombre like %?1%")
	ArrayList<Producto> buscarCampo(String campoBuscar);
	
	/*
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Producto p set p.cantidad = ?1, p.nombre = ?2, p.descripcion = ?3, p.precio = ?4 where p.id = ?5")
	void editarProducto(int cantidad, String nombre, String descripcion, double precio, int id);
	*/
}
