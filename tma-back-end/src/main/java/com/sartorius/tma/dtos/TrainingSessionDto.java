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
    private Long id;
    private UUID uuid;
    private LocalDate startDate;
    private LocalDate endDate;
    private TrainingSessionStatus status;
    private String place;
    private LocalTime startHour;
    private List<DocumentDto> documents;

    }

