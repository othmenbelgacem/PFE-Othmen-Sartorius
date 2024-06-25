package com.sartorius.tma.business.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.business.services.files.IFileService;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.repositories.MediaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaService {

  private final MediaRepository mediaRepository;

  private final IFileService dBFileStorageService;

  public List<Media> getAllMedias() {
    return this.mediaRepository.findAll();
  }

 public Media saveMedia(Media media) {
    return this.mediaRepository.save(media);
  }
  public Media findByUuid(UUID mediaUuid) {
    return this.mediaRepository.findByUuid(mediaUuid);
  }


  public void deleteMedia(Long id) {
    this.mediaRepository.deleteById(id);
  }

  public Media saveMedia(MultipartFile file, MediaContext context) throws Exception {

    String mediaName = dBFileStorageService.storeFile(file);
    String fileDownloadUri = "/file/downloadFile/" + mediaName;

    Media media = new Media();
    media.setMediaContext(context);
    media.setMediaUrl(fileDownloadUri);
    media.setMediaLabel(mediaName);
    media.setMediaSize(file.getSize());
    media.setMediaContentType(file.getContentType());
    media.setOriginalName(file.getOriginalFilename());
    media = mediaRepository.save(media);
    return media;
  }
}
