package com.sartorius.tma.client.dtos.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.dtos.AddressDto;
import com.sartorius.tma.dtos.MediaDetails;
import com.sartorius.tma.enumeration.RoleCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	private UUID userUuid;
	private String userEmail;
	private String userPhoneNumber;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String identifier;
	private RoleCode role;
	private AddressDto userAddress;
	private MultipartFile profilePicture;
	private MediaDetails userProfilePicture;

}
