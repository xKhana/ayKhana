package io.github.xkhana.ayKhana.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import io.github.xkhana.ayKhana.security.handler.CustomAccessDeniedHandler;
import io.github.xkhana.ayKhana.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.ByteArrayInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  @Order(1)
  public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/api/v1/auth/login")
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(basic -> basic.authenticationEntryPoint(customAuthenticationEntryPoint))
        .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler)).authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated())
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
            .authenticationEntryPoint(customAuthenticationEntryPoint)).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .logout(AbstractHttpConfigurer::disable)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(getOpenedResources()).permitAll()
            .requestMatchers("/api/v1/auth/register").permitAll()
            .anyRequest().authenticated())
        .build();
  }

  @Bean
  public RSAPublicKey jwtPublicKey(@Value("${jwt.public-key-string}") String publicKey) {
    return RsaKeyConverters.x509().convert(new ByteArrayInputStream(publicKey.getBytes()));
  }

  @Bean
  public RSAPrivateKey jwtPrivateKey(@Value("${jwt.private-key-string}") String privateKey) {
    return RsaKeyConverters.pkcs8().convert(new ByteArrayInputStream(privateKey.getBytes()));
  }

  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey jwtPublicKey) {
    return NimbusJwtDecoder.withPublicKey(jwtPublicKey).build();
  }

  @Bean
  public JwtEncoder jwtEncoder(RSAPrivateKey jwtPrivateKey, RSAPublicKey jwtPublicKey) {
    JWK jwk = new RSAKey.Builder(jwtPublicKey).privateKey(jwtPrivateKey).build();
    return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private String[] getOpenedResources() {
    return new String[]{
        "/swagger-ui/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/v3/api-docs",
        "/v3/api-docs/**"
    };
  }

}