package com.sartorius.tma.persistence.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_training_sub_stype")
@Data
@NoArgsConstructor
public class TrainingSubType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String label;
	@Column(columnDefinition = "TEXT")
	private String details;
	private int hourDuration;
	private int lifeDuration;
	private int integrationduration;
	@ManyToOne
	private TrainingType type;
	
	@ManyToMany
	private List<Trainer> trainers;
}
