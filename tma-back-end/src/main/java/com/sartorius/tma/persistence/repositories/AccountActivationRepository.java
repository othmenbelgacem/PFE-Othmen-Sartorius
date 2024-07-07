package com.sartorius.tma.persistence.repositories;

import com.sartorius.tma.persistence.entities.AccountActivation;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {

  Optional<AccountActivation> findByUuid(UUID activationCodeUuid);



}
