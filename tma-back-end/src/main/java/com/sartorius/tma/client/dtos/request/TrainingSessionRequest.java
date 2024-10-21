package com.sartorius.tma.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionRequest {
   private UUID trainingTypeUuid;
   private UUID trainingSubTypeUuid;
   private UUID trainerUuid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    private LocalDate endDate;
    private List<UUID> operatorUuids;
    private String place;
    private LocalTime startHour;
    private LocalTime endHour;

}
