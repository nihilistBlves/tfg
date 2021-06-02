package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Seguimiento;
import org.tfg.domain.Usuario;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
	
	@Query(value = "SELECT seguidor_id FROM seguimiento WHERE seguido_id = :id", nativeQuery = true)
	public Collection<Long> findSeguidoresByIdUsuario(@Param("id") Long id);
	
	@Query(value = "SELECT seguido_id FROM seguimiento WHERE seguidor_id = :id", nativeQuery = true)
	public Collection<Long> findSeguidosByIdUsuario(@Param("id") Long id);
	
	public Seguimiento getBySeguidoAndSeguidor(Usuario seguido, Usuario seguidor);

}
