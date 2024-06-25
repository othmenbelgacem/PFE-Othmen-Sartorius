package com.sartorius.tma.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_training")
@Data
@NoArgsConstructor
public class Training extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String title;
	private String details;
	private int hourDuration;
	private int lifeDuration;
	
	@ManyToOne
	private TrainingType trainingType;
	
	@ManyToOne
	private TrainingSubType trainingSubType;
}
