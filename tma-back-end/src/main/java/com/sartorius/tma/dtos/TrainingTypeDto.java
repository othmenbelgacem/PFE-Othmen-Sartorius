package com.sartorius.tma.dtos;

import com.sartorius.tma.persistence.entities.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto {

	private UUID uuid;
	private String label;
	public static TrainingTypeDto fromTraining(TrainingType t) {
		if(t == null) return null;
		return new TrainingTypeDto(t.getUuid(), t.getLabel());
	}
}
