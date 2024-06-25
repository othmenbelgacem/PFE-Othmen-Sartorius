package com.sartorius.tma.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.dtos.TrainerDto;
import com.sartorius.tma.dtos.TrainingSubTypeDto;
import com.sartorius.tma.dtos.TrainingTypeDto;
import com.sartorius.tma.dtos.UserDto;
import com.sartorius.tma.enumeration.TrainingSessionStatus;
import com.sartorius.tma.persistence.entities.TrainingSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSessionResponse {
    private UUID sessionId;
    private TrainerDto trainer;
    private TrainingTypeDto training;
    private TrainingSubTypeDto subTraining;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private LocalDate endDate;
    private List<UserDto> operators;
    private String operatorsName;
    private TrainingSessionStatus status;

    public static TrainingSessionResponse fromSession(TrainingSession session) {
        String operatorNames= "";
        if (session.getOperators() != null) {
             operatorNames = session.getOperators().stream()
                    .map(operator -> operator.getUserFirstName() + " " + operator.getUserLastName())
                    .collect(Collectors.joining(", "));}

        return TrainingSessionResponse.builder().sessionId(session.getUuid())
                .startDate(session.getStartDate()).endDate(session.getEndDate())
                .trainer(TrainerDto.fromTRainer(session.getTrainer()))
                .training(
                        TrainingTypeDto.fromTraining(session.getTrainingType()))
                .subTraining(TrainingSubTypeDto.fromTraining(
                        session.getTrainingSubType()))
                .operators(session.getOperators().stream()
                        .map(o -> UserDto.builder().userUuid(o.getUuid())
                                .userFirstName(o.getUserFirstName())
                                .userLastName(o.getUserLastName()).build())
                        .toList())
                .operatorsName(operatorNames)
                .status(session.getStatus())
                .build();
    }

}
