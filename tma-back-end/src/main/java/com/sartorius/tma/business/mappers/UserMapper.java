package com.sartorius.tma.business.mappers;

import java.util.List;

import com.sartorius.tma.enumeration.TrainingRequestStatus;
import org.springframework.stereotype.Component;

import com.sartorius.tma.business.services.UserUtils;
import com.sartorius.tma.client.dtos.response.TrainingOperatorResponse;
import com.sartorius.tma.client.dtos.response.UserResponse;
import com.sartorius.tma.client.dtos.response.User_Details;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.dtos.UserDto;
import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.entities.TrainingRequest;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.OperatorRepository;
import com.sartorius.tma.persistence.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

	private final UserUtils userUtils;
	private final UserRepository userRepository;
	private final OperatorRepository operatorRepository;

	public UserDto toUserDto(User user) {
		return new UserDto(user.getUuid(), user.getUserFirstName(), user.getUserLastName(), user.getCreatedAt());
	}

	public User toUser(UserDetails userDetails) {
		return userRepository.findByUuid(userDetails.getUserUuid()).orElse(null);
	}
	
	public Operator toOPerator(UserDetails userDetails) {
		return operatorRepository.findByUuid(userDetails.getUserUuid());
	}

	public User_Details toUserDetails(User user) {
		return new User_Details(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
				userUtils.getPictureProfile(user));
	}

	public UserDetails toUserDetailsResponse(User user) {
		return new UserDetails(user.getUuid(), user.getUserFirstName(), user.getUserLastName(), user.getUserEmail(),
				user.getUserPhoneNumber(), userUtils.getPictureProfile(user), user.getRole().getRoleCode(), user.getIdentifier()

		);
	}

	public UserResponse toUserResponse(User user) {
		return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(), user.getCreatedAt(),
				user.getUserEmail(), userUtils.getPictureProfile(user)

		);
	}
	
	public TrainingOperatorResponse toTrainingOperatorResponse(Operator operator, List<TrainingRequest> requests) {
		return new TrainingOperatorResponse(operator.getUuid(), operator.getUserFirstName(), operator.getUserLastName(),
				userUtils.getPictureProfile(operator), !requests.isEmpty() && requests.stream().anyMatch(request -> request.getStatus() != TrainingRequestStatus.SESSION_FINISHED)

		);
	}
}
