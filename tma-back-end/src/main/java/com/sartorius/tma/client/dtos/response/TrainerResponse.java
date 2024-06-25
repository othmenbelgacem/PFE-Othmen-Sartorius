package com.sartorius.tma.client.dtos.response;

import java.util.UUID;

import com.sartorius.tma.dtos.MediaDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
	private UUID uuid;
	private String fullName;
	private MediaDto picture; 

}
