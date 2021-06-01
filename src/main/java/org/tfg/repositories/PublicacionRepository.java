package org.tfg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

	public List<Publicacion> getByDuenioPublicacion (Usuario usuario);

	public Publicacion getById(Long idPublicacion);
}
