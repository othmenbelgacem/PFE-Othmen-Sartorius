package com.sartorius.tma.persistence.entities;

import java.util.List;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("trainer")
public class Trainer extends User {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	private String about;
	
	@ManyToMany
	private List<Training> trainings;
	
	@ManyToMany
    @JoinTable(
            name = "tma_training_type_trainers",
            joinColumns = @JoinColumn(name = "trainers_id"),
            inverseJoinColumns = @JoinColumn(name = "training_type_id")
        )
	private List<TrainingType> trainingTypes;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "tma_training_sub_stype_trainers",
			joinColumns = @JoinColumn(name = "trainers_id"),
			inverseJoinColumns = @JoinColumn(name = "training_sub_type_id")
	)
	private List<TrainingSubType> trainingSubTypes;
	
}
