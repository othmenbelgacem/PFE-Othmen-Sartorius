package com.sartorius.tma.dtos;

import java.util.UUID;

import com.sartorius.tma.persistence.entities.TrainingSubType;
import com.sartorius.tma.persistence.entities.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSubTypeDto {

  private UUID uuid;
  private String label;
  private String code;
  private TrainingTypeDto trainingType;

  public static TrainingSubTypeDto fromTraining(TrainingSubType t) {
    if(t == null ) return null;
    return TrainingSubTypeDto.builder().uuid(t.getUuid()).label(t.getLabel()).build();
  }
}
