package com.sartorius.tma.dtos.statics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionStatisticByTrainerOrOperatorDto {

    long totalSession;
    long totalInProgressSession;
    long totalDoneSession;
    String fullName;

    public TrainingSessionStatisticByTrainerOrOperatorDto(String fullName,
                                                          long totalDoneSession,
                                                          long totalSession,
                                                          long totalInProgressSession) {
        this.fullName = fullName;
        this.totalDoneSession = totalDoneSession;
        this.totalSession = totalSession;
        this.totalInProgressSession = totalInProgressSession;
    }

}