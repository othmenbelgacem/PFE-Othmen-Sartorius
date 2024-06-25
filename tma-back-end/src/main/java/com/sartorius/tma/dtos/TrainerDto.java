package com.sartorius.tma.dtos;

import java.util.UUID;

import com.sartorius.tma.enumeration.RoleCode;

import com.sartorius.tma.persistence.entities.Trainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
	private UUID userUuid;
	private String userFullName;

	public static TrainerDto fromTRainer(Trainer trainer) {
		if(trainer ==null) return null;
		return new TrainerDto(trainer.getUuid(), trainer.getUserFirstName()+" "+trainer.getUserLastName());
	}

}
