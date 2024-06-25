package com.sartorius.tma.client.dtos.response.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTokenResponse {
  private String token;
}
