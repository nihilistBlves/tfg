package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Seguimiento;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
	
	@Query("SELECT seguidor FROM Seguimiento WHERE seguido = :id")
	public Collection<Seguimiento> findSeguidoresByIdUsuario(@Param("id") Long id);
	
	@Query("SELECT seguido FROM Seguimiento WHERE seguidor = :id")
	public Collection<Seguimiento> findSeguidosByIdUsuario(@Param("id") Long id);

}
