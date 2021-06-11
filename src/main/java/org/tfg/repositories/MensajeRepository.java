package org.tfg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
	
	@Query(value = "SELECT * FROM mensaje WHERE mensaje.destinatario_id = :idUno AND mensaje.remitente_id = :idDos OR mensaje.remitente_id = :idUno AND mensaje.destinatario_id = :idDos", nativeQuery = true)
	public List<Mensaje> getMensajesChat(@Param("idUno")Long idUno, @Param("idDos")Long idDos);

}
