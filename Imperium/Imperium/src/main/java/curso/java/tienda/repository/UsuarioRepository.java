package curso.java.tienda.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import curso.java.tienda.model.Roles;
import curso.java.tienda.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	Usuario findByNombre(String nombre);
	
	ArrayList<Usuario> findAllByRol(Roles rol);
	
	/*
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Usuario u set u.nombre = ?1, u.nombreUsuario = ?2, u.apellido = ?3, u.email = ?4, u.direccion = ?5, u.localidad = ?6, u.provincia = ?7, u.pais = ?8, u.telefono = ?9, u.rol = ?10 where u.id = ?11")
	void editarUsuario(String nombre, String nombreUsuario, String apellido, String email, String direccion, String localidad, String provincia, String pais, String telefono, Roles rol, int id);
	*/
}
