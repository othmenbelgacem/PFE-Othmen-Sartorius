package com.sartorius.tma.persistence.repositories;


import com.sartorius.tma.persistence.entities.TrainingSession;
import com.sartorius.tma.persistence.entities.TrainingSessionPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionPresenceRepository extends JpaRepository<TrainingSessionPresence, Long> {
    //@Query("SELECT p FROM TrainingSessionPresence p WHERE p.operator.session = :session AND p.date = :date")
    //List<TrainingSessionPresence> findBySessionAndDate(@Param("session") TrainingSession session, @Param("date") LocalDate date);
}

