package com.sartorius.tma.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("operator")
public class Operator extends User {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	public Operator(User userIn) {
		setUserEmail(userIn.getUserEmail());
		setUserFirstName(userIn.getUserFirstName());
		setUserLastName(userIn.getUserLastName());
		setUserPassword(userIn.getUserPassword());
		setUserPhoneNumber(userIn.getUserPhoneNumber());
		setRole(userIn.getRole());
		setMedias(userIn.getMedias());
		setIdentifier(userIn.getIdentifier());
	}

	@Override
	public boolean equals(Object o) {
		Operator op = (Operator) o;
		return this.getUuid() == op.getUuid();
	}
}
