package org.tfg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.domain.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

}
