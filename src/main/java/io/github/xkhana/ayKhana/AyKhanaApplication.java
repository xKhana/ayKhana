package io.github.xkhana.ayKhana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AyKhanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AyKhanaApplication.class, args);
	}

}
