package com.sartorius.tma.client.dtos.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
	
	  private UUID uuid;
	  private String label;
	  private String details;
	  private int hourDuration;
	  private int lifeDuration;
	private int integrationduration;
	  private long subTypeCount;
	  private List<TrainerResponse> trainers;

}
