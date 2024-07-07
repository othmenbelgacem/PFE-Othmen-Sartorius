package com.sartorius.tma.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedFieldDto {
  private String fieldName;
  private boolean isValidate;

}
