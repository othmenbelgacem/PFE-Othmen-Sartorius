package com.sartorius.tma.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSessionPresencePerDateDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    private LocalDate date;
    private List<TrainingSessionPresenceDto> presences;
    private long presentceNumber;
    private long absenceNumber;

    public static List<TrainingSessionPresencePerDateDto> fromPresences(List<TrainingSessionPresenceDto> presences) {
        Map<LocalDate, List<TrainingSessionPresenceDto>> map =presences.stream().collect(Collectors.groupingBy(item -> item.getDate()));
        return map.entrySet().stream().map(entry -> new TrainingSessionPresencePerDateDto(entry.getKey(), entry.getValue(), entry.getValue().stream().filter(v -> v.isPresent()).count(),entry.getValue().stream().filter(v -> !v.isPresent()).count())).toList();
    }
}
