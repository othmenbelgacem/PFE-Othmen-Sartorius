package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.persistence.entities.TrainingSubType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingSubTypeRepository extends JpaRepository<TrainingSubType, Long> {

  TrainingSubType findByUuid(UUID uuid);

  List<TrainingSubType> findByTypeLabelOrderByLabel(String TrainingTypeLabel, Pageable pageable);

  List<TrainingSubType> findByTypeLabelIn(String[] TrainingTypeLabels, Pageable pageable);
  List<TrainingSubType> findByTypeLabelInAndLabelStartingWith(String[] TrainingTypeLabels,String TrainingSubTypeLabel, Pageable pageable);

  Page<TrainingSubType> findAllByLabelStartingWith(
      String TrainingSubTypeLabel, Pageable pageable);
  
  List<TrainingSubType> findByType_Uuid(UUID trainingTypeUuid, Sort sort);
}
