package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByRoleCode(RoleCode roleCode);
}
