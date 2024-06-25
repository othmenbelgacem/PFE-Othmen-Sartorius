package com.sartorius.tma.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.sartorius.tma.business.services.TrainingSessionService;
import com.sartorius.tma.business.services.files.DBFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.business.services.MediaService;
import com.sartorius.tma.business.services.UserService;
import com.sartorius.tma.business.services.files.IFileService;
import com.sartorius.tma.enumeration.MediaContext;
import com.sartorius.tma.persistence.entities.Media;
import com.sartorius.tma.persistence.entities.User;

import lombok.RequiredArgsConstructor;

import static com.mysql.cj.conf.PropertyKey.logger;

@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {

  private final IFileService dBFileStorageService;
  private final DBFileStorageService downloadFile;


  private final MediaService mediaService;

  private final UserService userService;
  private static final Logger logger = LoggerFactory.getLogger(FileController.class);


  @CrossOrigin
  @PostMapping("/post-media")
  public void uploadLogoFile(@RequestParam("file") MultipartFile[] files,
      @RequestParam(value = "contextUuid", required = false) UUID contextUuid,
      @RequestParam("context") MediaContext context) {
    List<Media> mediaList=new ArrayList<>();

    Arrays.stream(files).forEach(file -> {
      try {
        Media media = mediaService.saveMedia(file, context);
        mediaList.add(media);

         if (context == MediaContext.PICTURE_PROFIL) {
          User user = userService.getCurrentUser();
          if (!user.getMedias().isEmpty()) {
            Optional<Media> picture = user.getMedias().stream()
                .filter(userMedia -> userMedia.getMediaContext() == context)
                .findFirst();
            Media pictureToDelete = picture.isPresent() ? picture.get() : null;
            if (pictureToDelete != null) {
              user.getMedias().remove(pictureToDelete);
            }
          }
          user.getMedias().add(media);
          userService.saveOrUpdateUser(user);
        }


      } catch (Exception e) {
        e.printStackTrace();
      }

    });
 
    // selon le context, on fait le taraitement

  }


  @CrossOrigin
  @GetMapping("/downloadFile/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
      HttpServletRequest request)
      throws Exception {
    Resource resource = dBFileStorageService.loadFileAsResource(fileName);

    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {

    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

  @CrossOrigin
  @GetMapping("delete-user-photo")
  public void deleteUserPhoto(@RequestParam("context") MediaContext context) {
    User user = userService.getCurrentUser();
    if (!user.getMedias().isEmpty()) {
       if ( context == MediaContext.PICTURE_PROFIL ) {
        Optional<Media> picture = user.getMedias().stream()
            .filter(userMedia -> userMedia.getMediaContext() == context)
            .findFirst();
        Media pictureToDelete = picture.isPresent() ? picture.get() : null;
        if (pictureToDelete != null) {
          user.getMedias().remove(pictureToDelete);
        }
      }

    }
    userService.saveOrUpdateUser(user);
  }

  @CrossOrigin
  @RequestMapping(path = "/download-file", method = RequestMethod.GET)
  public ResponseEntity<Resource> download(@RequestParam("filename") String filepath) throws Exception {
    //Path path =dBFileStorageService.getAbsolutePath(filepath);
    Resource resource = dBFileStorageService.loadFileAsResource(filepath);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

}

