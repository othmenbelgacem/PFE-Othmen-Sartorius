package com.sartorius.tma.dtos;

import com.sartorius.tma.enumeration.EmailContext;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

 // private String to;

  private String subject;

  private String templateName;

  private Map<String, Object> maps;

  private Map<String, byte[]> attachments;

  private EmailContext emailContext;


}
