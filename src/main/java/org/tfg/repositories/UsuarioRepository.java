package org.tfg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Usuario getByLoginName(String loginName);
	public Usuario getByEmail(String email);
	

}
