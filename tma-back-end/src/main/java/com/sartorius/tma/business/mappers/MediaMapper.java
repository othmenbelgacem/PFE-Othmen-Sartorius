package com.sartorius.tma.business.mappers;

import com.sartorius.tma.dtos.MediaDto;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MediaMapper {

  private final MediaRepository mediaRepository;


}
