package com.sartorius.tma.dtos;

import com.sartorius.tma.enumeration.MediaContext;
import java.util.UUID;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 06/06/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
  private UUID mediaUuid;
  private String mediaUrl;
  private MediaContext mediaContext;

}
