package com.sartorius.tma.business.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.sartorius.tma.business.services.files.DBFileStorageService;
import com.sartorius.tma.client.dtos.request.TrainingSessionRequest;
import com.sartorius.tma.client.dtos.response.TrainingSessionResponse;
import com.sartorius.tma.dtos.DocumentDto;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainingSessionPresenceDto;
import com.sartorius.tma.dtos.TrainingSessionPresencePerDateDto;
import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.enumeration.TrainingRequestStatus;
import com.sartorius.tma.enumeration.TrainingSessionStatus;
import com.sartorius.tma.exceptions.DuplicateAttendanceException;
import com.sartorius.tma.persistence.entities.*;
import com.sartorius.tma.persistence.repositories.*;
import org.hibernate.Hibernate;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sartorius.tma.business.services.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingSessionService {

	private final TrainingSessionRepository trainingSessionRepository;
	private final TrainingTypeRepository trainingTypeRepository;
	private final TrainingSubTypeRepository trainingSubTypeRepository;
	private final TrainerRepository trainerRepository;
	private final TrainingRequestRepository trainingRequestRepository;
	private final UserService userService;
	private final OperatorService operatorService;
	private final EmailService emailService;
	private final TeamRepository TeamRepository;
	private final DBFileStorageService fileStorageService;
	private final DocumentRepository documentRepository;
	private static final Logger logger = LoggerFactory.getLogger(TrainingSessionService.class);



	TrainingSession getLastTrainingSessionsForAnOperatorForTrainingType(UUID trainingTypeUuid, Operator operator) {
		List<TrainingSession> sessions = trainingSessionRepository.findByTrainingTypeUuidAndOperatorsInOrderByCreatedAtDesc(
				trainingTypeUuid, List.of(operator));
		if(sessions != null && !sessions.isEmpty()) return sessions.get(0);
		return null;
	}


    public void saveSession(TrainingSessionRequest request) {
		TrainingType trainingType = trainingTypeRepository.findByUuid(request.getTrainingTypeUuid());
		TrainingSubType trainingSubType = request.getTrainingSubTypeUuid() != null ? trainingSubTypeRepository.findByUuid(request.getTrainingSubTypeUuid()) : null;
		Trainer trainer = trainerRepository.findByUuid(request.getTrainerUuid()).get();
		Pageable pageable = PageRequest.of(0, 10000, Sort.by("requestDate").descending());
		Page<TrainingRequest> trainingRequestPage;

		if (request.getTrainingSubTypeUuid() != null) {
			trainingRequestPage = trainingRequestRepository.findByTrainingSubType_UuidAndOperatorUuidIn((request.getTrainingSubTypeUuid()), request.getOperatorUuids(), pageable);
		} else {
			trainingRequestPage = trainingRequestRepository.findByTrainingType_UuidAndOperatorUuidIn(request.getTrainingTypeUuid(), request.getOperatorUuids(), pageable);
		}
		List<TrainingRequest> trainingRequests = trainingRequestPage.getContent();

		TrainingSession session = TrainingSession.builder().startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.trainingType(trainingType)
				.trainingSubType(trainingSubType)
				.trainer(trainer)
				.operators(trainingRequests.stream().map(TrainingRequest::getOperator).toList())
				.place(request.getPlace())
				.startHour(request.getStartHour())
				.trainingRequests(trainingRequests).build();

		session = trainingSessionRepository.save(session);

		trainingRequests.stream().forEach(trainingRequest -> trainingRequest.setStatus(TrainingRequestStatus.SESSION_PLANNED));
		trainingRequestRepository.saveAll(trainingRequests);

		// Send email to the team leader
		TrainingType trainingTypeName = trainingTypeRepository.findByUuid(request.getTrainingTypeUuid());
		TrainingSubType trainingSubTypeName = request.getTrainingSubTypeUuid() != null ? trainingSubTypeRepository.findByUuid(request.getTrainingSubTypeUuid()) : null;
		Trainer trainerName = trainerRepository.findByUuid(request.getTrainerUuid()).get();

		for (TrainingRequest trainingRequest : trainingRequests) {
			Operator operator = trainingRequest.getOperator();
			List<User> teamLeaders = findTeamLeadersByOperatorUuid(operator.getUuid());
			for (User teamLeader : teamLeaders) {
				if (teamLeader != null) {
					String emailBody = buildEmailContent(
							teamLeader.getUserFirstName(),
							operator,
							request.getStartDate(),
							request.getPlace(),
							request.getStartHour(),
							trainer.getUserFirstName() + " " + trainer.getUserLastName(),
							trainingTypeName.getLabel(),
							trainingSubTypeName != null ? trainingSubType.getLabel(): null
					);
					try {
						emailService.sendEmail(teamLeader.getUserEmail(), "Training Session Planned For"+" "+operator.getIdentifier(), emailBody);
					} catch (MessagingException e) {
						log.error("Failed to send email to team leader for operator {}", operator.getUserFirstName(), e);
					}
				}
			}
		}

	}
	private List<User> findTeamLeadersByOperatorUuid(UUID operatorUuid) {
		List<Team> teams = TeamRepository.findAllByMemberUuid(operatorUuid);
		return teams.stream().map(Team::getManager).collect(Collectors.toList());
	}
	private String buildEmailContent(String teamLeader, Operator operator, LocalDate startDate, String place, LocalTime startHour, String trainerName, String trainingTypeName, String trainingSubTypeName) {
		String emailTemplate = loadEmailTemplate();
		return emailTemplate.replace("${operatorName}", operator.getUserFirstName() + " " + operator.getUserLastName())
				.replace("${teamleader}",teamLeader)
				.replace("${startDate}", startDate.toString())
				.replace("${place}", place)
				.replace("${startHour}", startHour.toString())
				.replace("${trainerName}", trainerName)
				.replace("${trainingTypeName}", trainingTypeName)
				.replace("${trainingSubTypeName}", trainingSubTypeName != null ? trainingSubTypeName : "N/A");
	}

	private String loadEmailTemplate() {
		try (InputStream inputStream = getClass().getResourceAsStream("/templates/training-session-email.html")) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load email template", e);
		}
	}
	private String loadEmailTemplateAbsentOperators(String templateName) {
		try (InputStream inputStream = getClass().getResourceAsStream("/templates/" + templateName)) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load email template", e);
		}
	}

	public PageDto<TrainingSessionResponse> getSessions(Integer page, Integer offset) {
		Pageable pageable = PageRequest.of(page, offset, Sort.by("createdAt").descending());
		User user = userService.getCurrentUser();
		Page<TrainingSession> sessions = null;
		if(user.getRole().getRoleCode() == RoleCode.ADMINISTRATOR) {
			sessions=trainingSessionRepository.findAll(pageable);
			return new PageDto<>(sessions.getContent().stream().map(TrainingSessionResponse::fromSession).toList()
					, sessions.getTotalElements());
		}
		if(user.getRole().getRoleCode() == RoleCode.MANAGER) {
			sessions=trainingSessionRepository.findByTrainingRequestsTeamLeaderUuidOrderByCreatedAtDesc(user.getUuid(), pageable);
			return new PageDto<>(sessions.stream().map(TrainingSessionResponse::fromSession).toList(),
					sessions.getTotalElements());
		}
		if(user.getRole().getRoleCode() == RoleCode.TRAINER) {
			sessions=trainingSessionRepository.findByTrainerUuidOrderByCreatedAtDesc(user.getUuid(), pageable);
			return new PageDto<>(sessions.stream().map(TrainingSessionResponse::fromSession).toList(),
					sessions.getTotalElements());
		}
		if(user.getRole().getRoleCode() == RoleCode.OPERATOR){
			sessions = trainingSessionRepository.findByOperatorsUuidOrderByCreatedAtDesc(user.getUuid(),pageable);
			return new PageDto<>(sessions.stream().map(TrainingSessionResponse::fromSession).toList(),
					sessions.getTotalElements());
		}

		return null;
	}

	public void updateStatus(UUID sessionId, TrainingSessionStatus status) {
		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		session.setStatus(status);
		trainingSessionRepository.save(session);

		if(status == TrainingSessionStatus.DONE) {
			session.getTrainingRequests().forEach(request -> {
				request.setStatus(TrainingRequestStatus.SESSION_FINISHED);
				trainingRequestRepository.save(request);
			});
		}
	}

	public List<TrainingSessionPresenceDto> getSessionPresencesForASpecifidDate(UUID sessionId, LocalDate date) {
		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		if(session.getPresences() == null || session.getPresences().isEmpty()) {
		 return session.getOperators().stream()
				 .map(op ->TrainingSessionPresenceDto.initiate(op, date)).toList();
		} else {
			List<TrainingSessionPresence> targetPresences = session.getPresences().stream().filter(presence -> presence.getDate().equals(date)).toList();
			if(targetPresences.isEmpty()) {
				return session.getOperators().stream()
						.map(op ->TrainingSessionPresenceDto.initiate(op, date)).toList();
			}
			return targetPresences.stream()
					.map(TrainingSessionPresenceDto::fromEntity).toList();

		}
	}

	public List<TrainingSessionPresencePerDateDto>  getSessionPresences(UUID sessionId) {
		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		if(session.getPresences() != null && !session.getPresences().isEmpty()) {
			return TrainingSessionPresencePerDateDto.fromPresences(session.getPresences().stream()
					.map(TrainingSessionPresenceDto::fromEntity).toList());
		}
		return List.of();
	}


	public void savePresencesPerDate(UUID sessionId, List<TrainingSessionPresenceDto> body) {
		LocalDate date = body.get(0).getDate();

		// Check if session already has presences for the given date
		Optional<TrainingSession> existingSession = trainingSessionRepository.findSessionByUuidAndDate(sessionId, date);
		if (existingSession.isPresent()) {
			throw new DuplicateAttendanceException("You cannot create a new attendance file for that session. You already have one.");
		}

		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		List<TrainingSessionPresence> presences = body.stream().map(presence ->
				new TrainingSessionPresence(date, presence.isPresent(), operatorService.findByUuid(presence.getOperator().getUserUuid()))).toList();
		session.getPresences().addAll(presences);
		trainingSessionRepository.save(session);

		// Identify absent operators and their team leaders
		List<Operator> absentOperators = presences.stream()
				.filter(presence -> !presence.isPresent())
				.map(TrainingSessionPresence::getOperator)
				.collect(Collectors.toList());

		notifyTeamLeaders(absentOperators);
	}
	private void notifyTeamLeaders(List<Operator> absentOperators) {
		for (Operator operator : absentOperators) {
			List<User> teamLeaders = findTeamLeadersByOperatorUuid(operator.getUuid());
			for (User teamLeader : teamLeaders) {
				if (teamLeader != null) {
					sendEmailToTeamLeader(teamLeader, List.of(operator));
				}
			}
		}
	}
	private void sendEmailToTeamLeader(User teamLeader, List<Operator> operators) {
		String template = loadEmailTemplateAbsentOperators("absent-operators-email.html");

		StringBuilder operatorsList = new StringBuilder();
		for (Operator operator : operators) {
			operatorsList.append("<li>").append(operator.getUserFirstName()).append(" ").append(operator.getUserLastName()).append("</li>");
		}

		String emailBody = template.replace("${teamLeader}", teamLeader.getUserFirstName() + " " + teamLeader.getUserLastName())
				.replace("${date}", LocalDate.now().toString())
				.replace("${operators}", operatorsList.toString())
				.replace("${year}", String.valueOf(LocalDate.now().getYear()));

		String subject = "Absent Operators Notification";

		try {
			emailService.sendEmail(teamLeader.getUserEmail(), subject, emailBody);
			logger.info("Email sent to team leader {} about absent operators: {}", teamLeader.getUserEmail(), operators);
		} catch (MessagingException e) {
			logger.error("Failed to send email to team leader {}", teamLeader.getUserEmail(), e);
		}
	}
	@Transactional
	public void cancelAssociatedData(UUID sessionId) {
		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		if (session != null) {
			List<Operator> operators = session.getOperators();
			TrainingType trainingType = session.getTrainingType();
			TrainingSubType trainingSubType = session.getTrainingSubType();
			for (Operator operator : operators) {
				System.out.println(operator.getUserFirstName() + " " + operator.getUserLastName());
				List<TrainingRequest> trainingRequests;
				if (trainingSubType != null) {
					trainingRequests = trainingRequestRepository.findByTrainingSubType_UuidAndOperatorUuid(trainingSubType.getUuid(), operator.getUuid());
				} else {
					trainingRequests = trainingRequestRepository.findByTrainingType_UuidAndOperatorUuid(trainingType.getUuid(), operator.getUuid());
				}
				List<TrainingRequest> matchingRequests = trainingRequests.stream()
						.filter(request -> request.getStatus() != TrainingRequestStatus.REQUESTED)
						.collect(Collectors.toList());
				for (TrainingRequest request : matchingRequests) {
					request.setStatus(TrainingRequestStatus.REQUESTED);
				}
				trainingRequestRepository.saveAll(matchingRequests);
			}


			session.getOperators().clear();
			session.getTrainingRequests().clear();
			trainingSessionRepository.save(session);

			System.out.println("*************check if users are deleted from the session *******************");
			for (Operator operator : operators) {
				System.out.println(operator.getUserFirstName() + " " + operator.getUserLastName());
			}

			session.setStatus(TrainingSessionStatus.REJECTED);
			trainingSessionRepository.save(session);

		} else {
			System.out.println("Session not found for ID: " + sessionId);
		}
	}
	@Transactional
	public void saveDocuments(UUID sessionId, MultipartFile[] files) throws Exception {
		logger.info("Saving documents for session ID: {}", sessionId);

		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		if (session == null) {
			logger.error("Training session not found for ID: {}", sessionId);
			throw new Exception("Training session not found");
		}

		Hibernate.initialize(session.getDocuments()); // Initialize the lazy collection

		logger.info("Found training session: {}", session);

		for (MultipartFile file : files) {
			try {

				logger.info("Processing file: {}", file.getOriginalFilename());


				String storedFileName = fileStorageService.storeFile(file);


				Document document = new Document();
				document.setOriginalName(file.getOriginalFilename());
				document.setMediaLabel(storedFileName);
				document.setMediaSize(file.getSize());
				document.setMediaContentType(file.getContentType());
				document.setMediaUrl(storedFileName);
				document.setTrainingSession(session);


				logger.info("Created document: {}", document);


				session.getDocuments().add(document);

				logger.info("Document added to session: {}", document);
			} catch (Exception e) {
				logger.error("Error processing file: {}", file.getOriginalFilename(), e);
				throw e;
			}
		}

		// Save the session with the new documents
		try {
			trainingSessionRepository.save(session);
			logger.info("Training session saved with documents: {}", session.getDocuments());
		} catch (Exception e) {
			logger.error("Error saving training session: {}", session, e);
			throw e;
		}
	}

	// Method to get documents of a session
	@Transactional
	public List<DocumentDto> getDocuments(UUID sessionId) throws Exception {
		TrainingSession session = trainingSessionRepository.findByUuid(sessionId);
		if (session == null) {
			throw new Exception("Training session not found");
		}
		Hibernate.initialize(session.getDocuments());

		return session.getDocuments().stream()
				.map(DocumentDto::new)
				.collect(Collectors.toList());
	}

	// Method to download a document
	@Transactional
	public Resource downloadDocument(UUID documentId) throws Exception {
		Document document = documentRepository.findById(documentId)
				.orElseThrow(() -> new Exception("Document not found"));
		return fileStorageService.loadFileAsResource(document.getMediaUrl());
	}

}
