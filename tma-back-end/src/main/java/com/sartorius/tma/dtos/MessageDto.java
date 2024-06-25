package com.sartorius.tma.dtos;

import com.sartorius.tma.persistence.entities.User;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 05/12/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
  private String subject;
  private String body;

}
