package com.sartorius.tma.client.dtos.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeRequest {
	  private UUID trainingTypeUuid;
	  private String label;
	  private String details;
	  private int hourDuration;
	  private int lifeDuration;
	  private List<UUID> trainerUuids;
	  private int integrationduration ;
}
