package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tfg.domain.Seguimiento;
import org.tfg.domain.Usuario;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
	
	@Query(value = "SELECT seguidor_id FROM seguimiento WHERE seguido_id = :id AND aceptado = 1", nativeQuery = true)
	public Collection<Long> findSeguidoresByIdUsuario(@Param("id") Long id);
	
	@Query(value = "SELECT seguido_id FROM seguimiento WHERE seguidor_id = :id AND aceptado = 1", nativeQuery = true)
	public Collection<Long> findSeguidosByIdUsuario(@Param("id") Long id);
	
	@Query(value = "SELECT seguidor_id FROM seguimiento WHERE seguido_id = :id AND aceptado = 0", nativeQuery = true)
	public Collection<Long> findSeguidoresNoAceptadosByIdUsuario(@Param("id") Long id);
	
	@Query(value = "SELECT seguido_id FROM seguimiento WHERE seguidor_id = :id AND aceptado = 0", nativeQuery = true)
	public Collection<Long> findSeguidosNoAceptadosByIdUsuario(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM seguimiento WHERE seguidor_id = :idSeguidor AND seguido_id = :idSeguido", nativeQuery = true)
	public Seguimiento getSeguimientoParaBorrarSeguido(@Param("idSeguidor")Long idUserLogged, @Param("idSeguido")Long idSeguido);
	
	@Query(value = "SELECT * FROM seguimiento WHERE seguido_id = :idSeguido AND seguidor_id = :idSeguidor", nativeQuery = true)
	public Seguimiento getSeguimientoParaBorrarSeguidor(@Param("idSeguido")Long idUserLogged, @Param("idSeguidor")Long idSeguidor);


	public Seguimiento getBySeguidoAndSeguidor(Usuario seguido, Usuario seguidor);
	

}
