package com.sartorius.tma.dtos.training_request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.enumeration.TrainingRequestStatus;
import com.sartorius.tma.persistence.entities.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class TrainingRequestDTO {

    private UUID uuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private ZonedDateTime requestDate;

    private BaseEntityDTO teamLeader;

    private BaseEntityDTO operator;

    private BaseEntityDTO trainingType;

    private BaseEntityDTO trainingSubType;

    private TrainingRequestStatus status;

}
