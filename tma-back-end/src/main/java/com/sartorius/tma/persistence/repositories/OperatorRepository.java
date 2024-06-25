package com.sartorius.tma.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.persistence.entities.Operator;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
public Operator findByUuid(UUID uuid);

}
