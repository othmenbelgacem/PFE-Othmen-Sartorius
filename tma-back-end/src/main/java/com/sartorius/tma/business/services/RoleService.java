package com.sartorius.tma.business.services;

import com.sartorius.tma.persistence.entities.Role;
import com.sartorius.tma.persistence.repositories.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  public List<Role> getAllRoles() {
    return this.roleRepository.findAll();
  }

  public Role saveRole(Role role) {
    return this.roleRepository.save(role);
  }

  public void deleteRole(Long id) {
    this.roleRepository.deleteById(id);
  }
}
