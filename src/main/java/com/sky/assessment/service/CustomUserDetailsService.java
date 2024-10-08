package com.sky.assessment.service;

import com.sky.assessment.entity.User;
import com.sky.assessment.repository.UserRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("Loading user by email: {}", email);
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      log.error("Email not found.");
      throw new UsernameNotFoundException("User not found.");
    }

    return new org.springframework.security.core.userdetails.User(
        user.get().getEmail(),
        user.get().getPassword(),
        user.get().getRoles().stream()
              .map(role -> new SimpleGrantedAuthority(role.getName()))
              .collect(Collectors.toList())
    );
  }
}