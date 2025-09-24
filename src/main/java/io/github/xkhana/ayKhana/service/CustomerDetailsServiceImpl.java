package io.github.xkhana.ayKhana.service;

import io.github.xkhana.ayKhana.repository.CustomerRepository;
import io.github.xkhana.ayKhana.security.model.SecurityCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements UserDetailsService {
  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return customerRepository.findByUsername(username).map(SecurityCustomer::new).orElseThrow(
        () -> new UsernameNotFoundException("Invalid username or password.")
    );
  }
}
