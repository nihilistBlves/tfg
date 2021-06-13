package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

}
