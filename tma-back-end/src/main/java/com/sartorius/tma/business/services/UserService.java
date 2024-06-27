package com.sartorius.tma.business.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;

import javax.transaction.Transactional;

import com.sartorius.tma.persistence.entities.*;
import com.sartorius.tma.persistence.repositories.*;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sartorius.tma.business.mappers.MediaDatailsMapper;
import com.sartorius.tma.business.mappers.UserMapper;
import com.sartorius.tma.business.services.email.EmailService;
import com.sartorius.tma.client.dtos.request.ResetPasswordRequestDto;
import com.sartorius.tma.client.dtos.request.UserRequest;
import com.sartorius.tma.client.dtos.response.UserResponse;
import com.sartorius.tma.dtos.EmailDto;
import com.sartorius.tma.dtos.MediaDetails;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.enumeration.EmailContext;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.utils.Constants;
import com.sartorius.tma.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	private final PasswordEncoder encoder;

	private final RoleRepository roleRepository;
	
	private final MediaDatailsMapper mediaDatailsMapper;
	private final MediaService mediaService;

	private final ResetPasswordService resetPasswordService;
	private final EmailService emailService;
	private final TeamService teamService;

	private final TeamLeaderRepository teamLeaderRepository;
	private final OperatorRepository operatorRepository;
	private final AdminRepository adminRepository;




	public List<UserDetails> getAllUsers() {
		return this.userRepository.findAll().stream().map(userMapper::toUserDetailsResponse).collect(Collectors.toList());
	}

	public PageDto<UserDetails> getPagedUsers(int page, int offset, RoleCode userType, String text) {
		Pageable pageable = PageRequest.of(page, offset, Sort.by("userFirstName").ascending());
		Page<User> users = null;
		if(text != null) {
			if(userType == null) {
				users = this.userRepository.findByRoleRoleCodeNotAndUserFirstNameContainingOrIdentifierContaining(
						RoleCode.TRAINER, pageable, text, text);
			} else {
				users = this.userRepository.findByRoleRoleCodeAndRoleRoleCodeNotAndUserFirstNameContainingOrIdentifierContaining(
						userType, RoleCode.TRAINER, pageable, text, text);
			}
		} else {
			if(userType == null) {
				users = this.userRepository.findByRoleRoleCodeNot(RoleCode.TRAINER, pageable);
			} else {
				users = this.userRepository.findByRoleRoleCodeAndRoleRoleCodeNot(userType, RoleCode.TRAINER, pageable);
			}
		}

		return new PageDto<UserDetails>(
				users.getContent().stream().map(userMapper::toUserDetailsResponse).collect(Collectors.toList()),
				users.getTotalElements()
		);
	}


	public User getUserByUUID(UUID userUuid) {
		return this.userRepository.findByUuid(userUuid).orElse(null);
	}

	public User updateUser(UserRequest userRequest) throws Exception {
	User user;
			user=userRequest.getUserUuid()!=null?userRepository.findByUuid(userRequest.getUserUuid()).orElse(null):getCurrentUser();
	if(user!=null) {
		user.setUserEmail(userRequest.getUserEmail());
		user.setUserFirstName(userRequest.getUserFirstName());
		user.setUserLastName(userRequest.getUserLastName());
		user.setUserPhoneNumber(userRequest.getUserPhoneNumber());
		user.setIdentifier(userRequest.getIdentifier());
		if(userRequest.getRole()!=null){
		Role lodgerRole = this.roleRepository.findByRoleCode(userRequest.getRole());
		user.setRole(lodgerRole);};

		if (userRequest.getProfilePicture() != null) {
				user = this.deleteOldUserPicture(user.getUuid());
				Media media = mediaService
						.saveMedia(userRequest.getProfilePicture(), MediaContext.PICTURE_PROFIL);
				user.getMedias().add(media);
			
		}
		user=userRepository.save(user);
	}
		return user;
	}
	
	private User deleteOldUserPicture(UUID userUUid) {
		User user=userUUid!=null?userRepository.findByUuid(userUUid).orElse(null):getCurrentUser();
		Media oldProfilePic=user.getMedias().stream().filter(media -> media.getMediaContext().equals(MediaContext.PICTURE_PROFIL)).findFirst().orElse(null);
		if(oldProfilePic!=null) {
			user.getMedias().remove(oldProfilePic);
			user=userRepository.save(user);
			mediaService.deleteMedia(oldProfilePic.getId());
		}
		return user;
	}

	public void deleteUser(UUID userUuid) {
		User user = getUserByUUID(userUuid);
		this.userRepository.deleteById(user.getId());
	}

	public UserDetails getUserInfo() {
		User user = getCurrentUser();
		return user != null ? userMapper.toUserDetailsResponse(getCurrentUser()) : null;
	}

	public User getCurrentUser() {
		UUID currentuserId = SecurityUtil.getCurrentUserUuid();
		User user = getUserByUUID(currentuserId);
		return user;
	}

	public List<User> getUserDetailsyRole(RoleCode roleCode) {
		return userRepository.findByRoleRoleCode(roleCode);

	}

	public Boolean checkUserEmail(String userEmail) {
		log.info("Check existence of User Email: {}", userEmail);
		return userRepository.findByUserEmail(userEmail).isPresent();
	}

	public Boolean checkUserPhoneNumber(String PhoneNumber) {
		log.info("Check existence of User Phone Number: {}", PhoneNumber);
		return userRepository.findByUserPhoneNumber(PhoneNumber).isPresent();
	}

	public User getByUserEmail(String userEmail) {
		log.info("Check existence of User Email: {}", userEmail);
		return userRepository.findByUserEmail(userEmail).orElse(null);
	}
	public String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			result.append(characters.charAt(random.nextInt(characters.length())));
		}
		return result.toString();
	}

	public void saveUser(UserRequest userIn) {
		if (!checkUserEmail(userIn.getUserEmail())) {
			User user = new User();
			user.setUserEmail(userIn.getUserEmail());
			user.setUserFirstName(userIn.getUserFirstName());
			user.setUserLastName(userIn.getUserLastName());
			String randomString = generateRandomString(5);
			String password = userIn.getUserFirstName() + "-" + userIn.getUserLastName() + "-" + randomString;
			user.setUserPassword(encoder.encode(password));
			user.setUserPhoneNumber(userIn.getUserPhoneNumber());
			user.setIdentifier(userIn.getIdentifier());
			Role userRole = roleRepository.findByRoleCode(userIn.getRole());
			user.setRole(userRole);
			if (userIn.getProfilePicture() != null) {
				try {
					Media media = mediaService.saveMedia(userIn.getProfilePicture(), MediaContext.PICTURE_PROFIL);
					user.getMedias().add(media);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			switch (userRole.getRoleCode()) {
				case MANAGER:
					TeamLeader manager = new TeamLeader(user);
					UUID teamleader = teamLeaderRepository.save(manager).getUuid();
					sendPasswordEmail(teamleader, password);
					break;
				case OPERATOR:
					Operator operator = new Operator(user);
					UUID opera = operatorRepository.save(operator).getUuid();
					//sendPasswordEmail(opera, password);
					break;
				case ADMINISTRATOR:
					Administrator admin = new Administrator(user);
					UUID admino = adminRepository.save(admin).getUuid();
					sendPasswordEmail(admino, password);
					break;
			}
		}
	}

	public boolean isMatriculeUnique(String identifier) {
		return userRepository.findByIdentifier(identifier) == null;
	}


	public User saveOrUpdateUser(User user) {
		return this.userRepository.save(user);
	}
	 public MediaDetails getPictureProfile(User user){
	   return  mediaDatailsMapper.toMediaDetails(user.getMedias().stream()
	       .filter(media -> media.getMediaContext() == MediaContext.PICTURE_PROFIL)
	       .findFirst().orElse(null));

	 }

	public void sendPasswordEmail(UUID userUuid, String password) {
		try {
			User user = getUserByUUID(userUuid);
			String subject = "Welcome To Sartorius management Platform";

			String htmlTemplate = new String(IOUtils.toByteArray(new ClassPathResource("templates/email-template.html").getInputStream()));
			String body = htmlTemplate
					.replace("{{userFirstName}}", user.getUserFirstName())
					.replace("{{userIdentifier}}", user.getIdentifier())
					.replace("{{userEmail}}", user.getUserEmail())
					.replace("{{password}}", password);

			emailService.sendEmail(user.getUserEmail(), subject, body);
			log.info("Password reset email sent successfully to {}", user.getUserEmail());
		} catch (Exception e) {
			log.error("Error sending password reset email to user {}", userUuid, e);
			// Handle the exception, such as logging the error, notifying administrators, or retrying later.
		}
	}



	@Transactional
	public boolean resetPassword(ResetPasswordRequestDto dto) {
		//ResetPassword resetPassword = resetPasswordService.getByResetPasswordUuid(dto.getRequestUuid());
		User user = getCurrentUser();
		if(encoder.matches(dto.getOldPassword(),user.getUserPassword())) {
			user.setUserPassword(encoder.encode(dto.getPassword()));
			userRepository.saveAndFlush(user);
			return true;
		}else{
			return false;
		}
		//resetPasswordService.removeResetPassword(resetPassword);
	}

	public List<UserDetails> getTeamMembers(){
		User currentUser=getCurrentUser();
		Team team=null;
		if(currentUser.getRole().getRoleCode()==RoleCode.OPERATOR){
			team=teamService.getTeamByMember(currentUser);
		}else if (currentUser.getRole().getRoleCode()==RoleCode.MANAGER){
			 team=teamService.getTeamByManager(currentUser);
		}

		List<User> members=(team == null) ? Collections.emptyList() :team.getMembers().stream().filter(user -> user.getUuid()!=currentUser.getUuid()).collect(Collectors.toList());
		members.add(team.getManager());
		return members.stream().map(userMapper::toUserDetailsResponse).collect(Collectors.toList());

	}

	public List<UserResponse> getAllOperators() {
		return userRepository.findByRoleRoleCode(RoleCode.OPERATOR).stream()
				.map(userMapper::toUserResponse).toList();
	}
}
