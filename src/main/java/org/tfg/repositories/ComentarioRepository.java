package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{

}