package com.sartorius.tma.business.mappers;

import com.sartorius.tma.dtos.MediaDetails;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MediaDatailsMapper {
  private final MediaRepository mediaRepository;

  public MediaDetails toMediaDetails(Media media) {
     if(media!=null){
       return new MediaDetails(media.getUuid(), media.getMediaUrl(),media.getMediaLabel(),media.getOriginalName());
     }else return null;
  }

}
