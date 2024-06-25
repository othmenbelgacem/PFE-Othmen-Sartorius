package com.sartorius.tma.business.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.sartorius.tma.business.mappers.TrainingSubTypeMapper;
import com.sartorius.tma.business.mappers.TrainingTypeMapper;
import com.sartorius.tma.business.mappers.UserMapper;
import com.sartorius.tma.client.dtos.request.TrainingTypeRequest;
import com.sartorius.tma.client.dtos.response.TrainingOperatorResponse;
import com.sartorius.tma.client.dtos.response.TrainingSubTypeResponse;
import com.sartorius.tma.client.dtos.response.TrainingTypeResponse;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainingTypeDetails;
import com.sartorius.tma.dtos.TrainingTypeDto;
import com.sartorius.tma.dtos.TrainingTypeResponseDto;
import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.entities.Team;
import com.sartorius.tma.persistence.entities.TeamLeader;
import com.sartorius.tma.persistence.entities.Trainer;
import com.sartorius.tma.persistence.entities.TrainingRequest;
import com.sartorius.tma.persistence.entities.TrainingSession;
import com.sartorius.tma.persistence.entities.TrainingType;
import com.sartorius.tma.persistence.repositories.TrainerRepository;
import com.sartorius.tma.persistence.repositories.TrainingRequestRepository;
import com.sartorius.tma.persistence.repositories.TrainingTypeRepository;
import com.sartorius.tma.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeService {

	private final TrainingTypeRepository trainingTypeRepository;
	private final TrainerRepository trainerRepository;
	private final TrainingTypeMapper trainingTypeMapper;
	private final TrainingSubTypeMapper trainingSubTypeMapper;
	private final OperatorService operatorService;
	private final TeamLeaderService teamLeaderService;
	private final TrainingRequestService trainingRequestService;
	private final TrainingSessionService trainingSessionService;
	private final UserMapper userMapper;
	private final TrainingRequestRepository trainingRequestRepository;
	private final TeamService teamService;

	public List<TrainingTypeDto> getAllTrainingType() {
		return this.trainingTypeRepository.findAll().stream()
				.map(TrainingType -> trainingTypeMapper.toTrainingTypeDto(TrainingType)).collect(Collectors.toList());

	}

	public PageDto<TrainingTypeDto> getAllTrainingType(Integer page, Integer offset, String skillLevelLabel) {
		log.info("Get All paginated TrainingType");
		Page<TrainingType> skillLevels = null;
		Pageable pageable = null;
		if (offset == null) {
			pageable = PageRequest.of(page, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TrainingTypeLabel"));
		} else {
			pageable = PageRequest.of(page, offset, Sort.by(Sort.Direction.ASC, "TrainingTypeLabel"));
		}
		if (Strings.isNullOrEmpty(skillLevelLabel)) {
			skillLevels = trainingTypeRepository.findAll(pageable);
		} else {
			skillLevels = trainingTypeRepository.findByLabelStartingWith(skillLevelLabel, pageable);
		}

		return new PageDto<>(skillLevels.get().map(skillLevel -> trainingTypeMapper.toTrainingTypeDto(skillLevel))
				.collect(Collectors.toList()), skillLevels.getTotalElements());
	}

	public void addOrUpdate(TrainingTypeRequest trainingTypeRequest) {
		TrainingType trainingType;

		if (trainingTypeRequest.getTrainingTypeUuid() != null) {
			trainingType = trainingTypeRepository.findByUuid(trainingTypeRequest.getTrainingTypeUuid());

			if (trainingType != null) {
				trainingType.setIntegrationduration(trainingTypeRequest.getIntegrationduration());
				trainingType.setLabel(trainingTypeRequest.getLabel());
				trainingType.setDetails(trainingTypeRequest.getDetails());
				trainingType.setHourDuration(trainingTypeRequest.getHourDuration());
				trainingType.setLifeDuration(trainingTypeRequest.getLifeDuration());
			}
		} else {
			trainingType = new TrainingType();
			trainingTypeRequest.setIntegrationduration(trainingType.getIntegrationduration());
			trainingType.setLabel(trainingTypeRequest.getLabel());
			trainingType.setDetails(trainingTypeRequest.getDetails());
			trainingType.setHourDuration(trainingTypeRequest.getHourDuration());
			trainingType.setLifeDuration(trainingTypeRequest.getLifeDuration());
		}

		if (trainingTypeRequest.getTrainerUuids() != null) {
			List<UUID> trainerUuids = trainingTypeRequest.getTrainerUuids();
			List<Trainer> trainers = trainerUuids.stream().map(uuid -> trainerRepository.findByUuid(uuid).orElse(null))
					.filter(Objects::nonNull).collect(Collectors.toList());

			trainingType.setTrainers(trainers);
		}

		trainingTypeRepository.save(trainingType);
	}

	public PageDto<TrainingTypeResponseDto> getPagedTrainingTypes(int page, int offset, String text) {
		Pageable pageable = PageRequest.of(page, offset, Sort.by("label").ascending());
		Page<TrainingTypeDetails> trainingTypeDetailsPage = null;
		if(text == null) {
			trainingTypeDetailsPage = trainingTypeRepository.findAllWithSubTypeCount(pageable);
		} else {
			trainingTypeDetailsPage = trainingTypeRepository.findAllContainingTextWithSubTypeCount(pageable, text);
		}

		return new PageDto<>(trainingTypeDetailsPage.getContent().stream()
				.map(trainingTypeMapper::toTrainingTypeResponseDto).collect(Collectors.toList()),
				trainingTypeDetailsPage.getTotalElements());
	}

	public void deleteTrainingType(UUID trainingTypeUuid) {
		TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);

		if (trainingType != null) {
			trainingTypeRepository.delete(trainingType);
		}
	}

	public String getTrainingTypeLabel(UUID trainingTypeUuid) {
		TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);

		if (trainingType != null) {
			return trainingType.getLabel();
		}

		return "";
	}

	@Transactional
	public List<TrainingTypeResponse> getAllDetailedTrainingTypes(String text) {
		List<TrainingType> trainingList = new ArrayList<>();
		if(text != null) {
			trainingList =trainingTypeRepository.findByLabelContainingOrderByLabel(text);
		} else {
			trainingList = trainingTypeRepository.findAllByOrderByLabel();
		}
		return trainingList.stream().map(trainingTypeMapper::toTrainingTypeResponse).toList();
	}

	@Transactional
	public List<TrainingSubTypeResponse> getAllDetailedSubTrainingTypes(UUID trainingTypeUuid) {
		TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);
		return trainingType.getTrainingSubTypes().stream().map(trainingSubTypeMapper::toTrainingSubTypeResponse)
				.toList();
	}

	public String assignTrainingType(UUID trainingTypeUuid, UUID operatorUuid) {
		Operator operator = operatorService.findByUuid(operatorUuid);
		LocalDate requestDate = LocalDate.now();
		TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);

		TrainingSession lastTrainingSession = trainingSessionService
				.getLastTrainingSessionsForAnOperatorForTrainingType(trainingTypeUuid, operator);
		if(lastTrainingSession != null) {
			long monthsBetween = ChronoUnit.MONTHS.between(requestDate, lastTrainingSession.getEndDate());
			if (monthsBetween < trainingType.getLifeDuration()) {
				return Constants.CONFLICT;
			}
		}
		

		TeamLeader teamLeader = teamLeaderService.getCurrentTeamLeader();
		TrainingRequest trainingRequest = TrainingRequest.builder()
				.requestDate(requestDate)
				.operator(operator)
				.teamLeader(teamLeader)
				.trainingType(trainingType)
				.build();
		trainingRequestService.saveTrainingRequest(trainingRequest);
		return Constants.OK;
	}

	public List<TrainingOperatorResponse> getOperators(UUID trainingId) {
		TeamLeader teamLeader = teamLeaderService.getCurrentTeamLeader();
		Team team = teamService.getTeamByManager(teamLeader);
		List<Operator> operators = team.getMembers();
		List<TrainingOperatorResponse> operatorsResponse = operators.stream()
				.map(operator -> 
				{
					List<TrainingRequest> trainingRequests =  trainingRequestRepository.findByOperatorUuidAndTrainingTypeUuid(operator.getUuid(), trainingId);
					TrainingOperatorResponse operatorResponse =  userMapper.toTrainingOperatorResponse(operator, trainingRequests);
					return operatorResponse;
				}
				
				).toList();
		return operatorsResponse;

	}

	public TrainingTypeResponseDto getByUuid(UUID trainingTypeUuid) {
		TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);

		if (trainingType != null) {
			return TrainingTypeResponseDto.builder().label(trainingType.getLabel())
					.details(trainingType.getDetails()).build();
		}

		return new TrainingTypeResponseDto();
	}

	public String cancelTrainingType(UUID trainingTypeUuid, UUID operatorUuid) {
		// Retrieve the training request to be canceled
		TrainingRequest trainingRequest = trainingRequestRepository
				.findByTrainingTypeUuidAndOperatorUuid(trainingTypeUuid, operatorUuid);

		if (trainingRequest != null) {
			trainingRequestRepository.delete(trainingRequest);
			return Constants.OK;
		}

		return Constants.NOT_FOUND;
	}

}
