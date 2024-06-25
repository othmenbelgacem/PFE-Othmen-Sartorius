package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.dtos.TrainingTypeDetails;
import com.sartorius.tma.persistence.entities.TrainingType;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

  TrainingType findByUuid(UUID uuid);

  Page<TrainingType>  findByLabelStartingWith(String TrainingTypeLabel, Pageable pageable);

    List<TrainingType> findByLabelContainingOrderByLabel(String text);
    List<TrainingType> findAllByOrderByLabel();
  
  @Query("SELECT new com.sartorius.tma.dtos.TrainingTypeDetails(t, COUNT(ts)) " +
          "FROM TrainingType t LEFT JOIN t.trainingSubTypes ts " +
          "GROUP BY t")
   Page<TrainingTypeDetails> findAllWithSubTypeCount(Pageable pageable);

    @Query("SELECT new com.sartorius.tma.dtos.TrainingTypeDetails(t, COUNT(ts)) " +
            "FROM TrainingType t LEFT JOIN t.trainingSubTypes ts " +
            "WHERE t.label LIKE %:text% " +
            "GROUP BY t")
    Page<TrainingTypeDetails> findAllContainingTextWithSubTypeCount(Pageable pageable, @Param("text") String text);

}
