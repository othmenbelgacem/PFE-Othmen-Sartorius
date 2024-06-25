package com.sartorius.tma.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_admin")
@Data
@NoArgsConstructor
@DiscriminatorValue("admin")
public class Administrator extends User {
  public Administrator(User userIn) {
    setUserEmail(userIn.getUserEmail());
    setUserFirstName(userIn.getUserFirstName());
    setUserLastName(userIn.getUserLastName());
    setUserPassword(userIn.getUserPassword());
    setUserPhoneNumber(userIn.getUserPhoneNumber());
    setRole(userIn.getRole());
    setMedias(userIn.getMedias());
    setIdentifier(userIn.getIdentifier());
  }

  private static final long serialVersionUID = 5750899583047264016L;

}
