package com.sartorius.tma.persistence.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_training_type")
@Data
@NoArgsConstructor
public class TrainingType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String label;
	@Column(columnDefinition = "TEXT")
	private String details;
	private int hourDuration;
	private int lifeDuration;
	private  int integrationduration;
	@OneToMany(mappedBy = "type", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JsonIgnore
	private List<TrainingSubType> trainingSubTypes;
	
	@ManyToMany
	private List<Trainer> trainers;
}
