package com.sartorius.tma.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.persistence.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserEmail(String email);
  boolean existsByIdentifier(String identifier);
  User findByIdentifier(String identifier);
  Optional<User> findByUserPhoneNumber(String phoneNumber);
  List<User> findByRoleRoleCode(RoleCode roleCode);
  Page<User> findByRoleRoleCodeAndRoleRoleCodeNot(RoleCode roleCode, RoleCode roleCode2, Pageable pageable);
  Page<User> findByRoleRoleCodeAndRoleRoleCodeNotAndUserFirstNameContaining(RoleCode roleCode, RoleCode roleCode2, Pageable pageable, String text);

  Page<User> findByRoleRoleCodeNot(RoleCode roleCode, Pageable pageable);

  Page<User> findByRoleRoleCodeNotAndUserFirstNameContaining(RoleCode roleCode, Pageable pageable, String text);
  Optional<User> findByUuid(UUID uuid);

  Page<User> findByRoleRoleCodeAndRoleRoleCodeNotAndUserFirstNameContainingOrIdentifierContaining(
          RoleCode roleCode, RoleCode excludedRoleCode, Pageable pageable, String text, String identifier);

  Page<User> findByRoleRoleCodeNotAndUserFirstNameContainingOrIdentifierContaining(
          RoleCode roleCode, Pageable pageable, String text, String identifier);
}
