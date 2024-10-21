package com.sartorius.tma.business.services;

import com.sartorius.tma.business.mappers.TrainingSubTypeMapper;
import com.sartorius.tma.business.mappers.TrainingTypeMapper;
import com.sartorius.tma.business.mappers.UserMapper;
import com.sartorius.tma.client.dtos.request.TrainingSubTypeRequest;
import com.sartorius.tma.client.dtos.response.TrainingOperatorResponse;
import com.sartorius.tma.dtos.TrainingSubTypeDetails;
import com.sartorius.tma.dtos.TrainingTypeDto;
import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.entities.Team;
import com.sartorius.tma.persistence.entities.TeamLeader;
import com.sartorius.tma.persistence.entities.Trainer;
import com.sartorius.tma.persistence.entities.TrainingRequest;
import com.sartorius.tma.persistence.entities.TrainingSession;
import com.sartorius.tma.persistence.entities.TrainingSubType;
import com.sartorius.tma.persistence.entities.TrainingType;
import com.sartorius.tma.persistence.repositories.TrainerRepository;
import com.sartorius.tma.persistence.repositories.TrainingRequestRepository;
import com.sartorius.tma.persistence.repositories.TrainingSubTypeRepository;
import com.sartorius.tma.persistence.repositories.TrainingTypeRepository;
import com.sartorius.tma.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingSubTypeService {

	private final TrainingSubTypeRepository trainingSubTypeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
	private final TrainerRepository trainerRepository;
	private final TrainingSubTypeMapper trainingSubTypeMapper;
	private final TrainingTypeMapper trainingTypeMapper;
	private final OperatorService operatorService;
	private final TeamLeaderService teamLeaderService;
	private final TrainingRequestService trainingRequestService;
	private final TrainingSessionService trainingSessionService;
	private final UserMapper userMapper;
	private final TrainingRequestRepository trainingRequestRepository;
    private final TeamService teamService;
	
    public void add(TrainingSubTypeRequest trainingSubTypeRequest) {
        TrainingSubType trainingSubType = new TrainingSubType();

        if (trainingSubTypeRequest.getTrainingTypeUuid() != null) {

        	TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        	trainingTypeDto.setUuid(trainingSubTypeRequest.getTrainingTypeUuid());

            TrainingType trainingType = trainingTypeMapper.toTrainingType(trainingTypeDto);
            trainingSubType.setType(trainingType);
        }

        trainingSubType.setLabel(trainingSubTypeRequest.getLabel());
        trainingSubType.setDetails(trainingSubTypeRequest.getDetails());
        trainingSubType.setHourDuration(trainingSubTypeRequest.getHourDuration());
        trainingSubType.setLifeDuration(trainingSubTypeRequest.getLifeDuration());
        trainingSubType.setIntegrationduration(trainingSubTypeRequest.getIntegrationduration());
        
        if (trainingSubTypeRequest.getTrainerUuids() != null) {
            List<UUID> trainerUuids = trainingSubTypeRequest.getTrainerUuids();
            List<Trainer> trainers = trainerUuids.stream()
                                                .map(uuid -> trainerRepository.findByUuid(uuid).orElse(null))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList());

            trainingSubType.setTrainers(trainers);
        }

        trainingSubTypeRepository.save(trainingSubType);
    }
    
    public void update(TrainingSubTypeRequest trainingSubTypeRequest) {
        if (trainingSubTypeRequest.getUuid() != null) {

            TrainingSubType trainingSubType = trainingSubTypeRepository.findByUuid(trainingSubTypeRequest.getUuid());

            if (trainingSubType != null) {
                trainingSubType.setLabel(trainingSubTypeRequest.getLabel());
                trainingSubType.setDetails(trainingSubTypeRequest.getDetails());
                trainingSubType.setHourDuration(trainingSubTypeRequest.getHourDuration());
                trainingSubType.setLifeDuration(trainingSubTypeRequest.getLifeDuration());
                trainingSubType.setIntegrationduration(trainingSubTypeRequest.getIntegrationduration());
                
                if (trainingSubTypeRequest.getTrainerUuids() != null) {
                    List<UUID> trainerUuids = trainingSubTypeRequest.getTrainerUuids();
                    List<Trainer> trainers = trainerUuids.stream()
                                                        .map(uuid -> trainerRepository.findByUuid(uuid).orElse(null))
                                                        .filter(Objects::nonNull)
                                                        .collect(Collectors.toList());
                    
                    trainingSubType.setTrainers(trainers);
                }
                
                trainingSubTypeRepository.save(trainingSubType);
                
            }
        }
    }
        
    public List<TrainingSubTypeDetails> getTrainingSubTypesByTrainingType(UUID trainingTypeUuid, String text) {
        List<TrainingSubType> trainingSubTypes = trainingSubTypeRepository.findByType_Uuid(trainingTypeUuid, Sort.by("label").ascending());
		if(text != null) {
			trainingSubTypes = trainingSubTypes.stream().filter(t -> t.getLabel().contains(text)).toList();
		}
        return trainingSubTypes.stream()
                .map(trainingSubTypeMapper::toTrainingSubTypeDetails)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTrainingSubType(UUID trainingSubTypeUuid) {
        TrainingSubType trainingSubType = trainingSubTypeRepository.findByUuid(trainingSubTypeUuid);

        if (trainingSubType != null) {
            // Step 1: Remove the relationship with trainers
            List<Trainer> trainers = trainingSubType.getTrainers();
            if (trainers != null && !trainers.isEmpty()) {
                for (Trainer trainer : trainers) {
                    trainer.getTrainingSubTypes().remove(trainingSubType);
                    trainerRepository.save(trainer); // Save the trainer to update the relationship in the DB
                }
            }

            // Step 2: Remove the association from TrainingType
            TrainingType trainingType = trainingSubType.getType();
            if (trainingType != null) {
                trainingType.getTrainingSubTypes().remove(trainingSubType);
                trainingTypeRepository.save(trainingType); // Save the training type to update the relationship
            }

            // Step 3: Now delete the TrainingSubType
            trainingSubTypeRepository.delete(trainingSubType);
            System.out.println("Deleted subtraining: " + trainingSubTypeUuid);
        } else {
            System.out.println("Subtraining not found: " + trainingSubTypeUuid);
        }
    }


    public String assignSubTrainingType(UUID trainingSubTypeUuid,
                                        UUID operatorUuid) {
        Operator operator = operatorService.findByUuid(operatorUuid);
        TrainingSubType trainingSubType =
                trainingSubTypeRepository.findByUuid(trainingSubTypeUuid);
        ZonedDateTime requestDate = ZonedDateTime.now();
        TrainingSession lastTrainingSession = trainingSessionService
                .getLastTrainingSessionsForAnOperatorForTrainingType(
                        trainingSubTypeUuid, operator);
        if (lastTrainingSession != null) {
            long monthsBetween =
                    ChronoUnit.MONTHS.between(lastTrainingSession.getEndDate(),
                            requestDate);
            if (monthsBetween <= trainingSubType.getLifeDuration()) {
                return Constants.ALREADYDONE;
            }
        }


		TeamLeader teamLeader = teamLeaderService.getCurrentTeamLeader();
		TrainingType training = trainingSubType.getType();
		TrainingRequest trainingRequest = TrainingRequest.builder().requestDate(requestDate).operator(operator)
				.teamLeader(teamLeader).trainingType(training).trainingSubType(trainingSubType).build();
		trainingRequestService.saveTrainingRequest(trainingRequest);
		return Constants.OK;
	}

	public List<TrainingOperatorResponse> getOperators(UUID subTrainingId) {
		TeamLeader teamLeader = teamLeaderService.getCurrentTeamLeader();
		Team team = teamService.getTeamByManager(teamLeader);;
		List<Operator> operators = team.getMembers();
		List<TrainingOperatorResponse> operatorsResponse = operators.stream()
				.map(operator -> 
				{
					List<TrainingRequest> trainingRequests =  trainingRequestRepository.findByOperatorUuidAndTrainingSubTypeUuid(operator.getUuid(), subTrainingId);
					TrainingOperatorResponse operatorResponse =  userMapper.toTrainingOperatorResponse(operator, trainingRequests);
					return operatorResponse;
				}
				
				).toList();
		return operatorsResponse;
	}
}
