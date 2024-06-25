package com.sartorius.tma.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 23/10/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerifyDto {

  private String userEmail;

}
