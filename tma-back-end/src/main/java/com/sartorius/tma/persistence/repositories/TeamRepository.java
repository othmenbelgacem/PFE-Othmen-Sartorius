package com.sartorius.tma.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.persistence.entities.Team;
import com.sartorius.tma.persistence.entities.User;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findByUuid(UUID uuid);

	Team findByManager(User manager);

	@Query("SELECT t FROM Team t JOIN t.members m WHERE m.id = :memberId")
	Optional<Team> findByMember(@Param("memberId") Long memberId);
	@Query("SELECT t FROM Team t JOIN t.members m WHERE m.uuid = :memberUuid")
	Optional<Team> findByMemberUuid(@Param("memberUuid") UUID memberUuid);
	@Query("SELECT t FROM Team t JOIN t.members m WHERE m.uuid = :memberUuid")
	List<Team> findAllByMemberUuid(@Param("memberUuid") UUID memberUuid);
}
