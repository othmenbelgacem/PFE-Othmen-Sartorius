package com.sartorius.tma.business.services;

import org.springframework.stereotype.Service;

import com.sartorius.tma.business.mappers.MediaDatailsMapper;
import com.sartorius.tma.dtos.MediaDetails;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.persistence.entities.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserUtils {
private final MediaDatailsMapper mediaDatailsMapper;

 public MediaDetails getPictureProfile(User user){
   return  mediaDatailsMapper.toMediaDetails(user.getMedias().stream()
       .filter(media -> media.getMediaContext() == MediaContext.PICTURE_PROFIL)
       .findFirst().orElse(null));

 }

  





}
