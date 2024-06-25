package com.sartorius.tma.client.dtos.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.dtos.AddressDto;
import com.sartorius.tma.dtos.MediaDetails;
import com.sartorius.tma.enumeration.RoleCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest extends UserRequest {

	private String about;

}
