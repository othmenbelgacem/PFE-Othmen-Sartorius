package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.dtos.statics.TrainingRequestCountDTO;
import com.sartorius.tma.enumeration.TrainingRequestStatus;
import com.sartorius.tma.persistence.entities.TrainingRequest;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingRequestRepository
        extends JpaRepository<TrainingRequest, Long> {

    List<TrainingRequest> findByOperatorUuidAndTrainingTypeUuid(
            UUID operatorUuid, UUID trainingTypeUuid);

    TrainingRequest findByTrainingTypeUuidAndOperatorUuid(UUID trainingTypeUuid, UUID operatorUuid);

    List<TrainingRequest> findByOperatorUuidAndTrainingSubTypeUuid(
            UUID operatorUuid, UUID trainingSubTypeUuid);

    Page<TrainingRequest> findByTrainingType_UuidAndOperatorUuidIn(
            UUID trainingTypeId, List<UUID> operatorsUuids, Pageable pageable);

    List<TrainingRequest> findByTrainingType_Uuid(UUID trainingTypeId);

    Page<TrainingRequest> findByTrainingType_Uuid(UUID trainingTypeId,
                                                  Pageable pageable);

    Page<TrainingRequest> findByTrainingSubType_UuidAndOperatorUuidIn(
            UUID trainingSubTypeId, List<UUID> operatorsUuids,
            Pageable pageable);

    List<TrainingRequest> findByTrainingSubType_Uuid(UUID trainingSubTypeId);

    Page<TrainingRequest> findByTrainingSubType_Uuid(UUID trainingSubTypeId,
                                                     Pageable pageable);

    Page<TrainingRequest> findByTrainingType_UuidAndTeamLeaderUuid(
            UUID trainingTypeId, UUID teamLeaderUuId, Pageable pageable);

    Page<TrainingRequest> findByTrainingSubType_UuidAndTeamLeaderUuid(
            UUID trainingSubTypeId, UUID teamLeaderUuId, Pageable pageable);

    Page<TrainingRequest> findByTeamLeaderUuid(UUID teamLeaderUuId,
                                               Pageable pageable);

    @Query("SELECT COUNT(ts) FROM TrainingRequest ts WHERE ts.teamLeader.uuid = :teamLeaderId")
    long countAllSessions(@Param("teamLeaderId") UUID teamLeaderId);

    @Query("SELECT COUNT(ts) FROM TrainingRequest ts WHERE ts.status = 'SESSION_PLANNED' " +
            "AND ts.teamLeader.uuid = :teamLeaderId")
    long countPlannedSessions(@Param("teamLeaderId") UUID teamLeaderId);

    @Query("SELECT COUNT(ts) FROM TrainingRequest ts WHERE ts.status = 'SESSION_IN_PROGRESS'" +
            "AND ts.teamLeader.uuid = :teamLeaderId")
    long countInProgressSessions(@Param("teamLeaderId") UUID teamLeaderId);

    @Query("SELECT COUNT(ts) FROM TrainingRequest ts WHERE ts.status = 'SESSION_FINISHED'" +
            "AND ts.teamLeader.uuid = :teamLeaderId")
    long countDoneSessions(@Param("teamLeaderId") UUID teamLeaderId);



    @Query("SELECT new com.sartorius.tma.dtos.statics.TrainingRequestCountDTO(t.label, COUNT(r)) " +
            "FROM TrainingRequest r JOIN r.trainingType t " +
            "GROUP BY t.label " +
            "ORDER BY COUNT(r) DESC")
    List<TrainingRequestCountDTO> findTop10TrainingRequests();
    // New methods
    List<TrainingRequest> findByTrainingType_UuidAndOperatorUuid(UUID trainingTypeUuid, UUID operatorUuid);

    List<TrainingRequest> findByTrainingSubType_UuidAndOperatorUuid(UUID trainingSubTypeUuid, UUID operatorUuid);
    long countByStatus(TrainingRequestStatus status);
}
