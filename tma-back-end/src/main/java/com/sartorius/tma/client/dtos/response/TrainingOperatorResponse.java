package com.sartorius.tma.client.dtos.response;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.dtos.MediaDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingOperatorResponse {

	private UUID userUuid;
	private String userFirstName;
	private String userLastName;
	private MediaDetails userProfilePicture;
	private boolean isAlreadyRequestedForTheTraining;
}
