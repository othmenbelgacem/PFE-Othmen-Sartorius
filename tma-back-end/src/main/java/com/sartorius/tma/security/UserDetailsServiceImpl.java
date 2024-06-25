package com.sartorius.tma.security;


import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sartorius.tma.persistence.entities.Role;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String loginUserUSer) throws UsernameNotFoundException {
    User user = userRepository.findByUserEmail(loginUserUSer)
        .orElseThrow(
            () -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));
    return UserDetailsImpl.build(user);
  }

  @Transactional
  public Role getRolesUser(String loginUserUSer) {
    User user = userRepository.findByUserEmail(loginUserUSer)
        .orElseThrow(
            () -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));
    return user.getRole();
  }
}