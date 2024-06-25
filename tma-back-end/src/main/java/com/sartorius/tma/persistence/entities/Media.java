package com.sartorius.tma.persistence.entities;

import com.sartorius.tma.enumeration.MediaContext;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = -1593688143432377586L;

  private String mediaLabel;
  private Long mediaSize;
  private String mediaUrl;
  private String mediaContentType;
  @Enumerated(EnumType.STRING)
  private MediaContext mediaContext;
  private String originalName;

}
