package com.sartorius.tma.dtos.statics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class TrainingRequestCountDTO {
    private String trainingName;
    private long requestCount;
}
