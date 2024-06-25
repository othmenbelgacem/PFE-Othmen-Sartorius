package com.sartorius.tma.persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sartorius.tma.utils.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_user")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

  private static final long serialVersionUID = -5021940166648386759L;
  private String userEmail;
  private String userPhoneNumber;
  private String userPassword;
  private String userLogin;
  private String userFirstName;
  private String userLastName;
  private String identifier;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date userInscriptionDate;
  
  private boolean isActif;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Role role;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Media> medias = new ArrayList<>();
  
  @ManyToOne(cascade = CascadeType.MERGE)
  private Team team;
  
  @OneToOne(cascade = CascadeType.ALL)
  private Address address;


  @PrePersist
  public void prePersist() {
    this.userLogin = this.userEmail;
    this.isActif = true;
  }
}
