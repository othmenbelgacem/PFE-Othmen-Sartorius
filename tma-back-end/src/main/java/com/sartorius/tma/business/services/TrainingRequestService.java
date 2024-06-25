package com.sartorius.tma.business.services;

import com.sartorius.tma.business.mappers.TrainingRequestMapper;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.dtos.training_request.TrainingRequestDTO;
import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.enumeration.TrainingRequestStatus;
import com.sartorius.tma.persistence.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sartorius.tma.persistence.entities.TrainingRequest;
import com.sartorius.tma.persistence.repositories.TrainingRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingRequestService {

	private final TrainingRequestRepository trainingRequestRepository;
	private final TrainingRequestMapper trainingRequestMapper;
	private final UserService userService;

	public TrainingRequest saveTrainingRequest(TrainingRequest trainingRequest) {
		return trainingRequestRepository.save(trainingRequest);
	}
	public long countByStatus(TrainingRequestStatus status) {
		return trainingRequestRepository.countByStatus(status);
	}


	public PageDto<TrainingRequestDTO> getTrainingRequestPage(Integer page, Integer offset, String trainingId, String subTrainingId) {

		Pageable pageable = PageRequest.of(page, offset, Sort.by("requestDate").descending());
		Page<TrainingRequest> trainingRequestPage = null;
		User currentUser = userService.getCurrentUser();
		if(currentUser.getRole().getRoleCode() == RoleCode.MANAGER) {
			if (StringUtils.isNotBlank(subTrainingId)) {
				trainingRequestPage = trainingRequestRepository.findByTrainingSubType_UuidAndTeamLeaderUuid(UUID.fromString(subTrainingId), currentUser.getUuid(), pageable);
			} else if (StringUtils.isNotBlank(trainingId)) {
				trainingRequestPage = trainingRequestRepository.findByTrainingType_UuidAndTeamLeaderUuid(UUID.fromString(trainingId),currentUser.getUuid(), pageable);
			} else {
				trainingRequestPage = trainingRequestRepository.findByTeamLeaderUuid(currentUser.getUuid(), pageable);

			}
		} else if (currentUser.getRole().getRoleCode() == RoleCode.ADMINISTRATOR) {
			if (StringUtils.isNotBlank(subTrainingId)) {
				trainingRequestPage = trainingRequestRepository.findByTrainingSubType_Uuid(UUID.fromString(subTrainingId), pageable);
			} else if (StringUtils.isNotBlank(trainingId)) {
				trainingRequestPage = trainingRequestRepository.findByTrainingType_Uuid(UUID.fromString(trainingId), pageable);
			} else {
				trainingRequestPage = trainingRequestRepository.findAll(pageable);

			}
		}


		return new PageDto<>(
				trainingRequestPage != null ? trainingRequestPage.stream().map(trainingRequestMapper::trainingResponseToDTO).collect(Collectors.toList()): List.of(),
				trainingRequestPage != null ? trainingRequestPage.getTotalElements(): 0
        );
	}

	public List<UserDetails> getTrainingRequestsOpertaors(String trainingId, String subTrainingId) {
		List<TrainingRequest> list = new ArrayList<>();
		if (StringUtils.isNotBlank(subTrainingId)) {
			list = trainingRequestRepository.findByTrainingSubType_Uuid(UUID.fromString(subTrainingId));
		} else if (StringUtils.isNotBlank(trainingId)) {
			list = trainingRequestRepository.findByTrainingType_Uuid(UUID.fromString(trainingId));
		} else {
			list = trainingRequestRepository.findAll();

		}
		return list.stream().filter(request -> request.getStatus()== TrainingRequestStatus.REQUESTED).map(request -> request.getOperator()).distinct().map(operator -> UserDetails.builder().userFirstName(operator.getUserFirstName())
				.userLastName(operator.getUserLastName()).userUuid(operator.getUuid()).build()).toList();
	}
}
