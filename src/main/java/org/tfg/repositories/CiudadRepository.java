
package org.tfg.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Ciudad;



@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long>{

	@Query(value = "SELECT * FROM ciudad WHERE ciudad_name LIKE %:ciudadSeleccionada%", nativeQuery = true)
	public Collection<Ciudad> findCiudadesByCiudadName(@Param("ciudadSeleccionada") String ciudadSeleccionada);

}
