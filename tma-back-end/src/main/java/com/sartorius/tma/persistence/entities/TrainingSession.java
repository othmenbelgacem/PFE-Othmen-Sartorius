package com.sartorius.tma.persistence.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.enumeration.TrainingSessionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "tma_training_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSession extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private LocalDate startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private LocalDate endDate;
	@Enumerated(EnumType.STRING)
	private TrainingSessionStatus status;
	private String place;
	private LocalTime startHour;

	@ManyToOne
	private TrainingType trainingType;

	@ManyToOne
	private TrainingSubType trainingSubType;

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Operator> operators = new ArrayList<>();

	@ManyToOne
	private Trainer trainer;

	@OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Document> documents = new ArrayList<>();

	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TrainingRequest> trainingRequests = new ArrayList<>();

	@OneToMany(cascade = CascadeType.MERGE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TrainingSessionPresence> presences = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		if (this.presences == null) {
			this.presences = new ArrayList<>();
		}
		if (this.operators == null) {
			this.operators = new ArrayList<>();
		}
		if (this.trainingRequests == null) {
			this.trainingRequests = new ArrayList<>();
		}
		if (this.documents == null) {
			this.documents = new ArrayList<>();
		}
		this.status = TrainingSessionStatus.PLANNED;
		this.operators = this.trainingRequests.stream().map(TrainingRequest::getOperator).toList();
	}

	// Getter and setter for documents
	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
