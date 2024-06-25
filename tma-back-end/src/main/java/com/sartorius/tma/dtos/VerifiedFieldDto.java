package com.sartorius.tma.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 16/07/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedFieldDto {
  private String fieldName;
  private boolean isValidate;

}
