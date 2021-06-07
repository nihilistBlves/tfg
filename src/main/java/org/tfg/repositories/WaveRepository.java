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

	
	//Enero
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-01-01' AND '2021-02-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByEne(@Param("idUsuario") Long idUsuario);
	//Febrero
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-02-01' AND '2021-03-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByFeb(@Param("idUsuario") Long idUsuario);
	//Marzo
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-03-01' AND '2021-04-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByMar(@Param("idUsuario") Long idUsuario);
	//Abril
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-04-01' AND '2021-05-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByAbr(@Param("idUsuario") Long idUsuario);
	//Mayo
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-05-01' AND '2021-06-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByMay(@Param("idUsuario") Long idUsuario);
	//Junio
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-06-01' AND '2021-07-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByJun(@Param("idUsuario") Long idUsuario);
	//Julio
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-07-01' AND '2021-08-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByJul(@Param("idUsuario") Long idUsuario);
	//Agosto
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-08-01' AND '2021-09-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByAgo(@Param("idUsuario") Long idUsuario);
	//Septiembre
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-09-01' AND '2021-10-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedBySep(@Param("idUsuario") Long idUsuario);
	//Octubre
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-10-01' AND '2021-11-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByOct(@Param("idUsuario") Long idUsuario);
	//Noviembre
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-11-01' AND '2021-12-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByNov(@Param("idUsuario") Long idUsuario);
	//Diciembre
	@Query(value = "SELECT wave.id as wid FROM wave INNER JOIN publicacion ON wave.publicacion_waved_id = publicacion.id AND publicacion.duenio_publicacion_id = :idUsuario AND wave.fecha_like BETWEEN '2021-12-01' AND '2022-01-01'", nativeQuery = true)
	public Long[] idsPublicacionWavedByDic(@Param("idUsuario") Long idUsuario);
	

}
