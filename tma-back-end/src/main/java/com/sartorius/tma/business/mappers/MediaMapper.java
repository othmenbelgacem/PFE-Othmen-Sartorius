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

  public Media toMedia(MediaDto media) {
    return mediaRepository.findByUuid(media.getMediaUuid());
  }

	public MediaDto toMediaDto(Media media) {
    return new MediaDto(media.getUuid(), media.getMediaUrl(),media.getMediaContext());
  }

}
