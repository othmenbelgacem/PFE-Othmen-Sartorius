package com.sartorius.tma.business.mappers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sartorius.tma.client.dtos.response.TrainingTypeResponse;
import com.sartorius.tma.dtos.TrainingTypeDetails;
import com.sartorius.tma.dtos.TrainingTypeDto;
import com.sartorius.tma.dtos.TrainingTypeResponseDto;
import com.sartorius.tma.persistence.entities.TrainingType;
import com.sartorius.tma.persistence.repositories.TrainingTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainingTypeMapper {

	private final TrainingTypeRepository TrainingTypeRepository;
	private final TrainerMapper trainerMapper;

	public TrainingType toTrainingType(TrainingTypeDto TrainingTypeRequest) {
		return TrainingTypeRepository.findByUuid(TrainingTypeRequest.getUuid());
	}

	public TrainingTypeDto toTrainingTypeDto(TrainingType TrainingType) {
		return new TrainingTypeDto(TrainingType.getUuid(), TrainingType.getLabel());
	}

	public TrainingTypeResponseDto toTrainingTypeResponseDto(TrainingTypeDetails trainingTypeDetails) {
		return new TrainingTypeResponseDto(trainingTypeDetails.getTrainingType().getUuid(),
				trainingTypeDetails.getTrainingType().getLabel(), trainingTypeDetails.getTrainingType().getDetails(),
				trainingTypeDetails.getTrainingType().getHourDuration(),
				trainingTypeDetails.getTrainingType().getLifeDuration(), trainingTypeDetails.getSubTypeCount());
	}

	public TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType) {
		return new TrainingTypeResponse(trainingType.getUuid(), trainingType.getLabel(), trainingType.getDetails(),
				trainingType.getHourDuration(), trainingType.getLifeDuration(),trainingType.getIntegrationduration(),
				trainingType.getTrainingSubTypes().size(),
				trainingType.getTrainers().stream().map(trainerMapper::toTrainerResponse).toList());
	}
}
