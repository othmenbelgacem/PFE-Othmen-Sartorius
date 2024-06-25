package com.sartorius.tma.business.mappers;

import org.springframework.stereotype.Component;

import com.sartorius.tma.client.dtos.response.TrainingSubTypeResponse;
import com.sartorius.tma.dtos.TrainingSubTypeDetails;
import com.sartorius.tma.dtos.TrainingSubTypeDto;
import com.sartorius.tma.persistence.entities.TrainingSubType;
import com.sartorius.tma.persistence.repositories.TrainingSubTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainingSubTypeMapper {

	private final TrainingSubTypeRepository trainingSubTypeRepository;
	private final TrainingTypeMapper trainingTypeMapper;
	private final TrainerMapper trainerMapper;

	public TrainingSubType toTrainingSubType(TrainingSubTypeDto subTypeRequest) {
		return trainingSubTypeRepository.findByUuid(subTypeRequest.getUuid());
	}

	public TrainingSubTypeDto toTrainingSubTypeDto(TrainingSubType subType) {
		return TrainingSubTypeDto.builder().uuid(subType.getUuid()).label(subType.getLabel())
				.trainingType(trainingTypeMapper.toTrainingTypeDto(subType.getType())).build();
	}

	public TrainingSubTypeDetails toTrainingSubTypeDetails(TrainingSubType trainingSubType) {
		return TrainingSubTypeDetails.builder().uuid(trainingSubType.getUuid()).label(trainingSubType.getLabel())
				.details(trainingSubType.getDetails()).hourDuration(trainingSubType.getHourDuration())
				.lifeDuration(trainingSubType.getLifeDuration()).build();
	}

	public TrainingSubTypeResponse toTrainingSubTypeResponse(TrainingSubType trainingSubType) {
		return new TrainingSubTypeResponse(trainingSubType.getUuid(), trainingSubType.getLabel(),
				trainingSubType.getDetails(), trainingSubType.getHourDuration(), trainingSubType.getLifeDuration(),trainingSubType.getIntegrationduration(),
				trainingSubType.getTrainers().stream().map(trainerMapper::toTrainerResponse).toList());
	}
}
