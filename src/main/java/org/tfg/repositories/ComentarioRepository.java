package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Comentario;
import org.tfg.domain.Publicacion;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	
	public Collection <Comentario> getByPublicacionComentada(Publicacion p);

}