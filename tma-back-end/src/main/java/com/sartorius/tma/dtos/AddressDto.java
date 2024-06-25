package com.sartorius.tma.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

  private String street;
  private Integer streetNumber;
  private String postalCode;
  private String city;
  private String country;

}
