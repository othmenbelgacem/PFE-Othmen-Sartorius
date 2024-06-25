package com.sartorius.tma.business.services;

import com.sartorius.tma.exceptions.NotFoundException;
import com.sartorius.tma.persistence.entities.AccountActivation;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.AccountActivationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountActivationService {

  private final AccountActivationRepository accountActivationRepository;

  public AccountActivation getByAccountActivationCode(String activationCode) {
    log.info("Get AccountActivation with UUID: {}", activationCode);
    return accountActivationRepository.findByActivationCode(activationCode)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Aucun compte lié à ce code " + activationCode + " a été trouvé"));
  }


  public String saveAccountActivation(User user) {
    AccountActivation accountActivation = new AccountActivation();
    accountActivation.setUser(user);
    return accountActivationRepository.save(accountActivation).getActivationCode();
  }


  public void removeAccountActication(AccountActivation accountActivation) {
    log.info("Remove AccountActivation with UUID: {}", accountActivation.getUuid());
    accountActivationRepository.delete(accountActivation);
  }


}
