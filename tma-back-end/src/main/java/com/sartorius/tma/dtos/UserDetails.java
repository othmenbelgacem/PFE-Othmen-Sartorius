package com.sartorius.tma.dtos;

import java.util.UUID;

import com.sartorius.tma.enumeration.RoleCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {

	  protected UUID userUuid;
	  protected String userFirstName;
	  protected String userLastName;
	  protected String userEmail;
	  protected String userPhoneNumber;
	  protected MediaDetails userProfilePicture;
	  protected RoleCode userRole;
	  private String identifier;


}
