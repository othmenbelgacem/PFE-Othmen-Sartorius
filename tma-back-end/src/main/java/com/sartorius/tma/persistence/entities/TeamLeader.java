package com.sartorius.tma.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sartorius.tma.enumeration.MediaContext;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("team_leader")
public class TeamLeader extends User {

	private static final long serialVersionUID = 1L;

	public TeamLeader(User userIn) {
		setUserEmail(userIn.getUserEmail());
		setUserFirstName(userIn.getUserFirstName());
		setUserLastName(userIn.getUserLastName());
		setUserPassword(userIn.getUserPassword());
		setUserPhoneNumber(userIn.getUserPhoneNumber());
		setRole(userIn.getRole());
		setMedias(userIn.getMedias());
		setIdentifier(userIn.getIdentifier());
	}

}
