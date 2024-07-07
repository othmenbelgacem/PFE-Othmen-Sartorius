package com.sartorius.tma.persistence.entities;

import com.sartorius.tma.enumeration.RoleCode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tma_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {


  private static final long serialVersionUID = 9083326084374535654L;

  private String roleLabel;
  @Enumerated(EnumType.STRING)
  private RoleCode roleCode;
}
