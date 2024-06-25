package com.sartorius.tma.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.entities.TrainingSessionPresence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSessionPresenceDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
    private LocalDate date;
    private boolean present;

    @ManyToOne
    private UserDto operator;

    public static TrainingSessionPresenceDto fromEntity(TrainingSessionPresence entity) {
        return TrainingSessionPresenceDto.builder()
                .date(entity.getDate())
                .present(entity.isPresent())
                .operator(UserDto.builder().userUuid(entity.getOperator().getUuid())
                        .userFirstName(entity.getOperator().getUserFirstName()).userLastName(entity.getOperator().getUserLastName()).build())
                .build();
    }

    public static TrainingSessionPresenceDto initiate(Operator op, LocalDate date) {
        return TrainingSessionPresenceDto.builder()
                .date(date)
                .present(false)
                .operator(UserDto.builder().userUuid(op.getUuid())
                        .userFirstName(op.getUserFirstName()).userLastName(op.getUserLastName()).build())
                .build();
    }
}
