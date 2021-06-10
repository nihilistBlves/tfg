package org.tfg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	
	@Query(value = "SELECT * FROM publicacion WHERE publicacion.duenio_publicacion_id IN (SELECT seguimiento.seguido_id FROM seguimiento WHERE seguimiento.seguidor_id = :id)\n"
			+ "", nativeQuery = true)
	public List<Publicacion> feedDelUsuarioLogeado(@Param("id") Long id);

	public List<Publicacion> getByDuenioPublicacion (Usuario usuario);

	public Publicacion getById(Long idPublicacion);
}
