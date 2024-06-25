package com.sartorius.tma.dtos;

import com.sartorius.tma.enumeration.MediaContext;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDetails {
  private UUID mediaUuid;
  private String mediaUrl;
  private String mediaLabel;
  private String originalName;

}
