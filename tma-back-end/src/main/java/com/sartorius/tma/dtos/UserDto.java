package com.sartorius.tma.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

  private UUID userUuid;
  private String userFirstName;
  private String userLastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date userDateInscription;
}
