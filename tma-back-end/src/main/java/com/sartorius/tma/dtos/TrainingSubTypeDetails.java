package com.sartorius.tma.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSubTypeDetails {
	  private UUID uuid;
	  private String label;
	  private String details;
	  private int hourDuration;
	  private int lifeDuration;

}
