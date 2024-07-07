package com.sartorius.tma.dtos;

import com.sartorius.tma.enumeration.MediaContext;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
  private UUID mediaUuid;
  private String mediaUrl;
  private MediaContext mediaContext;

}
