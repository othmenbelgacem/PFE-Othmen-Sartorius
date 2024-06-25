package com.sartorius.tma.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.persistence.entities.TeamLeader;

@Repository
public interface TeamLeaderRepository extends JpaRepository<TeamLeader, Long> {
public TeamLeader findByUuid(UUID uuid);
}
