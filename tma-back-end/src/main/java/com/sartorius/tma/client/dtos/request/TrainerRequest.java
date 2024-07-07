package com.sartorius.tma.client.dtos.request;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequest extends UserRequest {

	private String about;

}
