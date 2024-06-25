package com.sartorius.tma.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.persistence.entities.Trainer;
import com.sartorius.tma.persistence.entities.TrainingSubType;
import com.sartorius.tma.persistence.entities.TrainingType;
import com.sartorius.tma.persistence.entities.User;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

	Optional<Trainer> findByUserEmail(String userEmail);
	Optional<Trainer> findByUuid(UUID uuid);
	 Page<Trainer> findByRoleRoleCode(RoleCode roleCode, Pageable pageable);
	 List<Trainer> findByTrainingTypes(TrainingType trainingType);
	 List<Trainer> findByTrainingSubTypes(TrainingSubType trainingSubType);

}
