package com.sartorius.tma.client.dtos.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgetPasswordRequestDto {
  private String userEmail;
}
