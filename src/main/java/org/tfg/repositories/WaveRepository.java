package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Publicacion;
import org.tfg.domain.Usuario;
import org.tfg.domain.Wave;

@Repository
public interface WaveRepository extends JpaRepository<Wave, Long>{

	public Wave getById(Long id);
	public Wave deleteByPublicacionWavedId(Long id);
	//public Wave deleteByDaWaveAndPublicacionWaved(Usuario usuario, Publicacion publicacion);
	public void deleteByDaWaveAndPublicacionWaved(Usuario attribute, Publicacion publicacion);
	
}
