package com.sartorius.tma.dtos.statics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSessionStatisticDto {

    private long totalSession;
    private long totalPlannedSession;
    private long totalInProgressSession;
    private long totalDoneSession;
    private int rejectedSession;
    private List<TrainingSessionStatisticByTrainerOrOperatorDto>
            sessionStaticByTrainerOrOperator;

    public TrainingSessionStatisticDto(long totalSession,
                                       long totalPlannedSession,
                                       long totalInProgressSession,
                                       long totalDoneSession) {

        this.totalSession = totalSession;
        this.totalPlannedSession = totalPlannedSession;
        this.totalInProgressSession = totalInProgressSession;
        this.totalDoneSession = totalDoneSession;
    }

}