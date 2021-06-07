package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	
	public Rol getByTipo(String tipo);
}

