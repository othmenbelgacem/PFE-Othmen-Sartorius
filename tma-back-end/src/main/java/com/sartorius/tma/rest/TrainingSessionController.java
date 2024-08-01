package com.sartorius.tma.rest;

import com.sartorius.tma.business.services.TrainingSessionService;
import com.sartorius.tma.business.services.files.DBFileStorageService;
import com.sartorius.tma.client.dtos.request.TrainingSessionRequest;
import com.sartorius.tma.client.dtos.response.TrainingSessionResponse;
import com.sartorius.tma.dtos.DocumentDto;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainingSessionPresenceDto;
import com.sartorius.tma.dtos.TrainingSessionPresencePerDateDto;
import com.sartorius.tma.enumeration.TrainingSessionStatus;
import com.sartorius.tma.exceptions.DuplicateAttendanceException;
import com.sartorius.tma.persistence.entities.Document;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@CrossOrigin
@RequestMapping("/training-session")
@RequiredArgsConstructor
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;
    private final DBFileStorageService downloadFile;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping
    public void saveSession(
            @RequestBody TrainingSessionRequest request) {
        this.trainingSessionService.saveSession(request);
    }

    @GetMapping
    public PageDto<TrainingSessionResponse> getSessions(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "status", required = false) TrainingSessionStatus status
    ) {
        return this.trainingSessionService.getSessions(page, offset, status);
    }


    @PutMapping("update-status/{uuid}/{sessionStatus}")
    public void updateSessionStatus(
            @PathVariable("uuid") UUID sessionId,
            @PathVariable("sessionStatus") TrainingSessionStatus sessionStatus) {
        if (sessionStatus == TrainingSessionStatus.REJECTED) {
            this.trainingSessionService.cancelAssociatedData(sessionId);
        }
        this.trainingSessionService.updateStatus(sessionId, sessionStatus);
    }

    @GetMapping("presences/{uuid}")
    public List<TrainingSessionPresencePerDateDto> getSessionPresences(
            @PathVariable("uuid") UUID sessionId) {
        return this.trainingSessionService.getSessionPresences(sessionId);
    }

    @GetMapping("presences-by-date/{uuid}")
    public List<TrainingSessionPresenceDto> getSessionPresencesForASpecifidDate(
            @PathVariable("uuid") UUID sessionId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return this.trainingSessionService.getSessionPresencesForASpecifidDate(sessionId, date);
    }

    @PostMapping("add-presences/{uuid}")
    public ResponseEntity<?> savePresencesPerDate(
            @PathVariable("uuid") UUID sessionId,
            @RequestBody List<TrainingSessionPresenceDto> body) {
        try {
            trainingSessionService.savePresencesPerDate(sessionId, body);
            return ResponseEntity.ok().build();
        } catch (DuplicateAttendanceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("{sessionId}/upload-documents")
    public ResponseEntity<?> uploadDocuments(@PathVariable UUID sessionId,
                                             @RequestParam("files") MultipartFile[] files) {
        try {
            trainingSessionService.saveDocuments(sessionId, files);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload documents");
        }
    }

    @GetMapping("/{sessionId}/documents")
    public ResponseEntity<List<DocumentDto>> getDocuments(@PathVariable UUID sessionId) {
        try {
            List<DocumentDto> documents = trainingSessionService.getDocuments(sessionId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{sessionId}/documents/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID sessionId, @RequestParam("fileName") String fileName) {
        logger.info("Received download request for sessionId: {}, fileName: {}", sessionId, fileName); // Debug log
        try {
            Resource resource = downloadFile.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error downloading file: {}", e.getMessage(), e); // Log errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

