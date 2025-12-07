package io.github.xkhana.ayKhana.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.security.interfaces.RSAPublicKey;

@RestController
public class ProductController {
  private final RSAPublicKey jwtPublicKey;

  public ProductController(RSAPublicKey jwtKeysProperties) {
    this.jwtPublicKey = jwtKeysProperties;
  }

  @GetMapping("/products")
  public String products(Principal principal) {
    System.out.println(jwtPublicKey.toString());
    return "sllnnam" + principal.getName();
  }

  @GetMapping
  public String home() {
    return "home";
  }

}
