package org.tfg.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Comentario;
import org.tfg.domain.Publicacion;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	
	public List <Comentario> getByPublicacionComentada(Publicacion p);

}