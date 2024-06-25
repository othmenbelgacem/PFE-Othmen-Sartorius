package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.persistence.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Long> {

}
