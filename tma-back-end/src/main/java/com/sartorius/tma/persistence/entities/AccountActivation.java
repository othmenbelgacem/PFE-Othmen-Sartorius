package com.sartorius.tma.persistence.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tma_account_activation")
@Data
@NoArgsConstructor
public class AccountActivation extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;


  @ManyToOne
  private User user;
  private String activationCode;

  @PrePersist
  private void persistActivationCode() {
    this.activationCode = UUID.randomUUID().toString();
    //this.activationCode = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
  }


}
