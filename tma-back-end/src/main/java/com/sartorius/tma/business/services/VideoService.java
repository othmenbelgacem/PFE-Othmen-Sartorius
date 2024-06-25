package com.sartorius.tma.business.services;
import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sartorius.tma.business.services.files.IFileService;
import com.sartorius.tma.persistence.entities.Media;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rokaya
 * @Date 01/07/2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {

  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_LENGTH = "Content-Length";
  public static final String VIDEO_CONTENT = "video/";
  public static final String CONTENT_RANGE = "Content-Range";
  public static final String ACCEPT_RANGES = "Accept-Ranges";
  public static final String BYTES = "bytes";
  public static final int CHUNK_SIZE = 314700;
  public static final int BYTE_RANGE = 1024;

  private final IFileService dbFileStorageService;
  private final MediaService mediaService;



  /**
     * Prepare the content.
     *
     * @param mediaUuid UUID.
     * @param range    String.
     * @return ResponseEntity.
 * @throws Exception 
     */
    public ResponseEntity<byte[]> prepareContent(UUID mediaUuid,final String range) throws Exception {

      try {
        Media media=mediaService.findByUuid(mediaUuid);
        final String fileKey = media.getMediaLabel();
        long rangeStart = 0;
        long rangeEnd = CHUNK_SIZE;
        final Long fileSize = dbFileStorageService.getFileSize(fileKey);
        if (range == null) {
          return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
              .header(CONTENT_TYPE, VIDEO_CONTENT + fileKey.substring(fileKey.lastIndexOf(".") + 1))
              .header(ACCEPT_RANGES, BYTES)
              .header(CONTENT_LENGTH, String.valueOf(rangeEnd))
              .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
              .header(CONTENT_LENGTH, String.valueOf(fileSize))
              .body(dbFileStorageService.readByteRangeNew(fileKey, rangeStart, rangeEnd)); // Read the object and convert it as bytes
        }
        String[] ranges = range.split("-");
        rangeStart = Long.parseLong(ranges[0].substring(6));
        if (ranges.length > 1) {
          rangeEnd = Long.parseLong(ranges[1]);
        } else {
          rangeEnd = rangeStart + CHUNK_SIZE;
        }

        rangeEnd = Math.min(rangeEnd, fileSize - 1);
        final byte[] data = dbFileStorageService.readByteRangeNew(fileKey, rangeStart, rangeEnd);
        final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
        if (rangeEnd >= fileSize) {
          httpStatus = HttpStatus.OK;
        }
        return ResponseEntity.status(httpStatus)
            .header(CONTENT_TYPE, VIDEO_CONTENT + fileKey.substring(fileKey.lastIndexOf(".") + 1))
            .header(ACCEPT_RANGES, BYTES)
            .header(CONTENT_LENGTH, contentLength)
            .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
            .body(data);
      } catch (IOException e) {
        log.error("Exception while reading the file {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }


    }

}
