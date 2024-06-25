package com.sartorius.tma.persistence.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_team")
@Data
@NoArgsConstructor
public class Team extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String teamName;
	@Fetch(value = FetchMode.SUBSELECT)
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<Operator> members;
	@OneToOne
	private User manager;

}
