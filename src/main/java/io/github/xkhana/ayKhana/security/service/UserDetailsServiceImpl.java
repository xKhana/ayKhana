package io.github.xkhana.ayKhana.security.service;

import io.github.xkhana.ayKhana.repository.UserRepository;
import io.github.xkhana.ayKhana.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).map(SecurityUser::new).orElseThrow(
        () -> new UsernameNotFoundException("Invalid username or password.")
    );
  }
}
