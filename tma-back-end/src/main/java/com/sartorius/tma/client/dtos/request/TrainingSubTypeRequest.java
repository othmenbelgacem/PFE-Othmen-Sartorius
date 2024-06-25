package com.sartorius.tma.client.dtos.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSubTypeRequest {
	  private UUID uuid;
	  private String label;
	  private String details;
	  private int hourDuration;
	  private int lifeDuration;
	private int integrationduration;
	  private UUID trainingTypeUuid;
	  private List<UUID> trainerUuids;

}
