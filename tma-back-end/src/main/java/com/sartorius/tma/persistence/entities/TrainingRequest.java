package com.sartorius.tma.persistence.entities;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.enumeration.TrainingRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_training_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRequest extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private ZonedDateTime requestDate;
	
	@ManyToOne
	private TeamLeader teamLeader;
	
	@ManyToOne
	private Operator operator;
	
	@ManyToOne
	private TrainingType trainingType;
	
	@ManyToOne
	private TrainingSubType trainingSubType;
	
	@Enumerated(EnumType.STRING)
	private TrainingRequestStatus status;

	@PrePersist
	public void prePersist() {
		this.status= TrainingRequestStatus.REQUESTED;
	}



}
