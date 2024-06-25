package com.sartorius.tma.business.mappers;

import org.springframework.stereotype.Component;

import com.sartorius.tma.business.services.UserUtils;
import com.sartorius.tma.client.dtos.response.TrainerResponse;
import com.sartorius.tma.client.dtos.response.UserResponse;
import com.sartorius.tma.client.dtos.response.User_Details;
import com.sartorius.tma.dtos.MediaDto;
import com.sartorius.tma.dtos.TrainerDetails;
import com.sartorius.tma.dtos.TrainerDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.dtos.UserDto;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.entities.Trainer;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerMapper {

	private final UserUtils userUtils;
	private final UserRepository userRepository;

	public UserDto toUserDto(User user) {
		return new UserDto(user.getUuid(), user.getUserFirstName(), user.getUserLastName(), user.getCreatedAt());
	}

	public User toUser(UserDetails userDetails) {
		return userRepository.findByUuid(userDetails.getUserUuid()).orElse(null);
	}

	public User_Details toUserDetails(User user) {
		return new User_Details(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
				userUtils.getPictureProfile(user));
	}

	public TrainerDetails toUserDetailsResponse(Trainer user) {
		return new TrainerDetails(user.getUuid(),user.getIdentifier(), user.getUserFirstName(), user.getUserLastName(), user.getUserEmail(),
				user.getUserPhoneNumber(), userUtils.getPictureProfile(user), user.getRole().getRoleCode(),
				user.getAbout()

		);
	}

	public UserResponse toUserResponse(User user) {
		return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(), user.getCreatedAt(),
				user.getUserEmail(), userUtils.getPictureProfile(user)

		);
	}

	public TrainerDto toTrainerDto(Trainer user) {
		String fullName = user.getUserFirstName() + " " + user.getUserLastName();
		return new TrainerDto(user.getUuid(), fullName);
	}

	public TrainerResponse toTrainerResponse(Trainer trainer) {
		Media media = trainer.getMedias() != null && !trainer.getMedias().isEmpty()  ? trainer.getMedias().get(0) : null;
		MediaDto picture = media != null
				? new MediaDto(media.getUuid(), "/api/file/downloadFile/" + media.getMediaLabel(),
						MediaContext.PICTURE_PROFIL)
				: null;
		return new TrainerResponse(trainer.getUuid(), trainer.getUserFirstName()+" " + trainer.getUserLastName(), picture);

	}
}
