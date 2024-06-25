package com.sartorius.tma.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.persistence.entities.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

}
