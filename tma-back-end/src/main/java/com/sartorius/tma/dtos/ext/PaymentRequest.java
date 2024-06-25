package com.sartorius.tma.dtos.ext;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
	private double paymentAmount;
	private String paymentClientFullName;
	private String paymentCardNumber;
//	@NotEmpty
//	@Size(min = 3,max =3,  message = "the payment card secret number should have 3 digits ")
	private String paymentCardSecretNumber;
	private Date paymentCardExpiredDate;

}
