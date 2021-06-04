package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.domain.Wave;

@Repository
public interface WaveRepository extends JpaRepository<Wave, Long> {

	public Wave getById(Long id);

	public Wave deleteByPublicacionWavedId(Long id);

	public void deleteByDaWaveAndPublicacionWaved(Usuario attribute, Publicacion publicacion);

	@Query(value = "SELECT wave.publicacion_waved_id FROM wave WHERE wave.da_wave_id = :idUsuario", nativeQuery = true)
	public Long[] idsPublicacionWavedByUser(@Param("idUsuario") Long idUsuario);

}
