package com.sartorius.tma.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.enumeration.TrainingSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tma_training_session_presence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSessionPresence extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private LocalDate date;
	@Column(columnDefinition="bit default 0")
	private boolean present = false;
	
	@ManyToOne
	private Operator operator;
}
