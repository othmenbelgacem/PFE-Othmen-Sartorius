package com.sartorius.tma.business.services;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sartorius.tma.business.services.email.EmailService;
import com.sartorius.tma.persistence.repositories.*;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sartorius.tma.business.mappers.TrainerMapper;
import com.sartorius.tma.client.dtos.request.TrainerRequest;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainerDetails;
import com.sartorius.tma.dtos.TrainerDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.entities.Role;
import com.sartorius.tma.persistence.entities.Trainer;
import com.sartorius.tma.persistence.entities.TrainingSubType;
import com.sartorius.tma.persistence.entities.TrainingType;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

	private final TrainerRepository trainerRepository;
	private final TrainingTypeRepository trainingTypeRepository;
	private final TrainingSubTypeRepository trainingSubTypeRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;
	private final MediaService mediaService;
	private final TrainerMapper trainerMapper;
	private final UserService UserService;
	private final EmailService emailService;
	private  final UserRepository userRepository;
	public List<TrainerDetails> getAllTrainers() {
		return this.trainerRepository.findAll().stream().map(trainerMapper::toUserDetailsResponse).collect(Collectors.toList());
	}

	public void saveUser(TrainerRequest userIn) {
		if (!checkUserEmail(userIn.getUserEmail())) {
			Trainer user = new Trainer();
			user.setIdentifier(userIn.getIdentifier());
			user.setUserEmail(userIn.getUserEmail());
			user.setUserFirstName(userIn.getUserFirstName());
			user.setUserLastName(userIn.getUserLastName());
			String password = userIn.getUserFirstName() + "-" + userIn.getUserLastName();
			user.setUserPassword(encoder.encode(password));
			user.setUserPhoneNumber(userIn.getUserPhoneNumber());
			Role lodgerRole = roleRepository.findByRoleCode(RoleCode.TRAINER);
			user.setRole(lodgerRole);
			user.setAbout(userIn.getAbout());
			;
			if (userIn.getProfilePicture() != null) {
				try {
					Media media = mediaService.saveMedia(userIn.getProfilePicture(), MediaContext.PICTURE_PROFIL);
					user.getMedias().add(media);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			UUID userUuid = trainerRepository.save(user).getUuid();
			sendPasswordEmail(userUuid,password);

		}
	}
	public boolean isMatriculeUnique(String identifier) {
		return trainerRepository.findByIdentifier(identifier) == null;
	}
	public void sendPasswordEmail(UUID userUuid, String password) {
		try {
			User user = getUserByUUID(userUuid);
			String subject = "Welcome To Sartorius management Platform";

			String htmlTemplate = new String(IOUtils.toByteArray(new ClassPathResource("templates/email-template.html").getInputStream()));
			String body = htmlTemplate
					.replace("{{userIdentifier}}", user.getIdentifier())
					.replace("{{userFirstName}}", user.getUserFirstName())
					.replace("{{userEmail}}", user.getUserEmail())
					.replace("{{password}}", password);

			emailService.sendEmail(user.getUserEmail(), subject, body);
			log.info("Password reset email sent successfully to {}", user.getUserEmail());
		} catch (Exception e) {
			log.error("Error sending password reset email to user {}", userUuid, e);

		}
	}
	public User updateUser(TrainerRequest userRequest) throws Exception {
		Trainer user;
		user = userRequest.getUserUuid() != null ? trainerRepository.findByUuid(userRequest.getUserUuid()).orElse(null)
				: getCurrentTrainer();
		if (user != null) {
			user.setIdentifier(userRequest.getIdentifier());
			user.setUserEmail(userRequest.getUserEmail());
			user.setUserFirstName(userRequest.getUserFirstName());
			user.setUserLastName(userRequest.getUserLastName());
			user.setUserPhoneNumber(userRequest.getUserPhoneNumber());

			if (userRequest.getProfilePicture() != null) {
				user = this.deleteOldUserPicture(user.getUuid());
				Media media = mediaService.saveMedia(userRequest.getProfilePicture(), MediaContext.PICTURE_PROFIL);
				user.getMedias().add(media);

			}
			user = trainerRepository.save(user);
		}
		return user;
	}

	public void deleteTrainer(UUID userUuid) {
		Trainer user = getUserByUUID(userUuid);
		this.trainerRepository.deleteById(user.getId());
	}

	public TrainerDetails getUserInfo() {
		Trainer user = getCurrentTrainer();
		return user != null ? trainerMapper.toUserDetailsResponse(user) : null;
	}

	public Boolean checkUserEmail(String userEmail) {
		log.info("Check existence of User Email: {}", userEmail);
		return trainerRepository.findByUserEmail(userEmail).isPresent();
	}

	public Trainer getCurrentTrainer() {
		UUID currentuserId = SecurityUtil.getCurrentUserUuid();
		Trainer user = getUserByUUID(currentuserId);
		return user;
	}

	public Trainer getUserByUUID(UUID userUuid) {
		return this.trainerRepository.findByUuid(userUuid).orElse(null);
	}

	private Trainer deleteOldUserPicture(UUID userUUid) {
		Trainer user = userUUid != null ? trainerRepository.findByUuid(userUUid).orElse(null) : getCurrentTrainer();
		Media oldProfilePic = user.getMedias().stream()
				.filter(media -> media.getMediaContext().equals(MediaContext.PICTURE_PROFIL)).findFirst().orElse(null);
		if (oldProfilePic != null) {
			user.getMedias().remove(oldProfilePic);
			user = trainerRepository.save(user);
			mediaService.deleteMedia(oldProfilePic.getId());
		}
		return user;
	}
	
	public PageDto<TrainerDetails> getPagedUsers(int page,int offset, RoleCode userType) {
		Pageable pageable = PageRequest.of(page, offset, Sort.by("userFirstName").ascending());
		Page<Trainer> users = null;
		if(userType == null) {
			users = this.trainerRepository.findAll(pageable);
		} else {
			users = this.trainerRepository.findByRoleRoleCode(userType, pageable);
		}
		
		return new PageDto<TrainerDetails>(users.getContent().stream().map(trainerMapper::toUserDetailsResponse).toList(),users.getTotalElements());
	}

    public List<TrainerDto> retrieveAllTrainers() {
        List<Trainer> allTrainers = trainerRepository.findAll();

        return allTrainers.stream()
            .map(trainerMapper::toTrainerDto)
            .collect(Collectors.toList());
    }
    
    public List<TrainerDto> getTrainersByTrainingTypeUuid(UUID trainingTypeUuid) {
        TrainingType trainingType = trainingTypeRepository.findByUuid(trainingTypeUuid);
        
        if (trainingType != null) {
            List<Trainer> trainers = trainerRepository.findByTrainingTypes(trainingType);

            return trainers.stream()
                    .map(trainerMapper::toTrainerDto)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
    
    public List<TrainerDto> getTrainersByTrainingSubTypeUuid(UUID trainingSubTypeUuid) {
        TrainingSubType subTrainingType = trainingSubTypeRepository.findByUuid(trainingSubTypeUuid);
      
        if (subTrainingType != null) {
            List<Trainer> trainers = trainerRepository.findByTrainingSubTypes(subTrainingType);

            return trainers.stream()
                    .map(trainerMapper::toTrainerDto)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
