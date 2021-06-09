package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Mensaje;
import org.tfg.domain.Usuario;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
	
	@Query(value = "SELECT * FROM mensaje WHERE ", nativeQuery = true)
	public Collection<Mensaje> getMensajesByDosUsuarios(Usuario usuarioUno, Usuario usuarioDos);

}
