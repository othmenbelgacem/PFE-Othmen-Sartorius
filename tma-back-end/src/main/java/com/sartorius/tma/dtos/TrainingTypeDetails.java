package com.sartorius.tma.dtos;

import java.util.UUID;

import com.sartorius.tma.persistence.entities.TrainingType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDetails {
    private TrainingType trainingType;
    private long subTypeCount;

}
