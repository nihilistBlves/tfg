package org.tfg.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Instrumento;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
	
	@Query(value = "SELECT * FROM instrumento WHERE instrumento.nombre IN (:instrumentosStrings)", nativeQuery = true)
	public Collection<Instrumento> getInstrumentosByArray(@Param("instrumentosStrings") List<String> instrumentosStrings);

}
