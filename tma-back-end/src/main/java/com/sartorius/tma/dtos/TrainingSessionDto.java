package com.sartorius.tma.dtos;

import com.sartorius.tma.enumeration.TrainingSessionStatus;
import com.sartorius.tma.persistence.entities.TrainingSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionDto {
    private Long id;  // Changed to Long to match BaseEntity
    private LocalDate startDate;
    private LocalDate endDate;
    private TrainingSessionStatus status;
    private String place;
    private LocalTime startHour;
    private List<DocumentDto> documents;

    public TrainingSessionDto(TrainingSession session) {
        this.id = session.getId();  // Ensure id is properly set
        this.startDate = session.getStartDate();
        this.endDate = session.getEndDate();
        this.status = session.getStatus();
        this.place = session.getPlace();
        this.startHour = session.getStartHour();
        this.documents = session.getDocuments().stream()
                .map(DocumentDto::new)
                .collect(Collectors.toList());
    }

    }

