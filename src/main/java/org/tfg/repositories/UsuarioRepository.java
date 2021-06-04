package org.tfg.repositories;

import java.util.Collection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.tfg.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Usuario getByLoginName(String loginName);
	public Usuario getByEmail(String email);
	@Query(value = "SELECT * FROM usuario WHERE login_name LIKE %:busqueda%", nativeQuery = true)
	public Collection<Usuario> findUsuariosByLoginNameBusqueda(@Param("busqueda") String busqueda);
}
