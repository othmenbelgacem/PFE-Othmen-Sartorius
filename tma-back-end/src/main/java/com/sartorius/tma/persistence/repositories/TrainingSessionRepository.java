package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.dtos.statics.TrainingSessionStatisticByTrainerOrOperatorDto;
import com.sartorius.tma.dtos.statics.TrainingSessionStatisticDto;
import com.sartorius.tma.enumeration.TrainingSessionStatus;
import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.entities.TrainingSession;
import com.sartorius.tma.persistence.entities.TrainingType;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingSessionRepository
        extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findByTrainingTypeUuidAndOperatorsInOrderByCreatedAtDesc(
            UUID trainingTypeUuid, List<Operator> operators);


    Page<TrainingSession> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<TrainingSession> findByOperatorsUuidOrderByCreatedAtDesc(@Param("operatorUuid") UUID operatorUuid, Pageable pageable);

    Page<TrainingSession> findByTrainingRequestsTeamLeaderUuidOrderByCreatedAtDesc(
            UUID teamLeaderUuid, Pageable pageable);

    Page<TrainingSession> findByTrainerUuidOrderByCreatedAtDesc(
            UUID trainerUuid, Pageable pageable);

    TrainingSession findByUuid(UUID id);

    // New methods to support status filtering
    Page<TrainingSession> findByStatusOrderByCreatedAtDesc(TrainingSessionStatus status, Pageable pageable);

    Page<TrainingSession> findByTrainingRequestsTeamLeaderUuidAndStatusOrderByCreatedAtDesc(
            UUID teamLeaderUuid, TrainingSessionStatus status, Pageable pageable);

    Page<TrainingSession> findByTrainerUuidAndStatusOrderByCreatedAtDesc(
            UUID trainerUuid, TrainingSessionStatus status, Pageable pageable);

    Page<TrainingSession> findByOperatorsUuidAndStatusOrderByCreatedAtDesc(
            UUID operatorUuid, TrainingSessionStatus status, Pageable pageable);
    @Query("SELECT COUNT(ts) FROM TrainingSession ts")
    long countAllSessions();
    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.status = 'REJECTED'")
    int countRejectedSessions();
    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.status = 'PLANNED'")
    long countPlannedSessions();

    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.status = 'IN_PROGRESS'")
    long countInProgressSessions();

    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.status = 'DONE'")
    long countDoneSessions();
    @Query("SELECT new com.sartorius.tma.dtos.statics.TrainingSessionStatisticByTrainerOrOperatorDto(" +
            "CONCAT(ts.trainer.userFirstName, ' ', ts.trainer.userLastName), " +
            "SUM(CASE WHEN ts.status = 'DONE' THEN 1 ELSE 0 END), " +
            "COUNT(ts), " +
            "SUM(CASE WHEN ts.status = 'IN_PROGRESS' THEN 1 ELSE 0 END)) " +
            "FROM TrainingSession ts " +
            "GROUP BY ts.trainer.userFirstName, ts.trainer.userLastName")
    List<TrainingSessionStatisticByTrainerOrOperatorDto> findTrainerSessionStatisticsByTrainer();

    @Query("SELECT new com.sartorius.tma.dtos.statics.TrainingSessionStatisticDto(" +
            "COUNT(ts), " +
            "SUM(CASE WHEN ts.status = 'IN_PROGRESS' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ts.status = 'DONE' THEN 1 ELSE 0 END)) " +
            "FROM TrainingSession ts WHERE ts.trainer.uuid = :trainerId")
    TrainingSessionStatisticDto findTrainerSessionStatisticsByTrainerId(
            @Param("trainerId") UUID trainerId);

    @Query("SELECT ts FROM TrainingSession ts JOIN ts.operators op WHERE op.id = :operatorId")
    List<TrainingSession> findAllByOperatorId(@Param("operatorId") Long operatorId);

    @Query("SELECT s FROM TrainingSession s JOIN s.presences p WHERE s.uuid = :sessionId AND p.date = :date")
    Optional<TrainingSession> findSessionByUuidAndDate(@Param("sessionId") UUID sessionId, @Param("date") LocalDate date);

    // Queries to fetch statistics for a specific operator
    @Query("SELECT COUNT(ts) FROM TrainingSession ts JOIN ts.operators op WHERE op.uuid = :operatorId")
    long countAllSessionsByOperator(@Param("operatorId") UUID operatorId);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts JOIN ts.operators op WHERE ts.status = 'PLANNED' AND op.uuid = :operatorId")
    long countPlannedSessionsByOperator(@Param("operatorId") UUID operatorId);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts JOIN ts.operators op WHERE ts.status = 'IN_PROGRESS' AND op.uuid = :operatorId")
    long countInProgressSessionsByOperator(@Param("operatorId") UUID operatorId);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts JOIN ts.operators op WHERE ts.status = 'DONE' AND op.uuid = :operatorId")
    long countDoneSessionsByOperator(@Param("operatorId") UUID operatorId);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts JOIN ts.operators op WHERE ts.status = 'REJECTED' AND op.uuid = :operatorId")
    long countRejectedSessionsByOperator(@Param("operatorId") UUID operatorId);
    @Query("SELECT ts FROM TrainingSession ts LEFT JOIN FETCH ts.documents WHERE ts.uuid = :uuid")
    TrainingSession findByUuidWithDocuments(@Param("uuid") UUID uuid);
}


