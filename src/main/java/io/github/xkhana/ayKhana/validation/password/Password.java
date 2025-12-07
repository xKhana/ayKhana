package io.github.xkhana.ayKhana.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface Password {
  String message() default "{validation.password}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int min() default 12;

  CharTypes[] shouldContain() default {CharTypes.LOWERCASE, CharTypes.UPPERCASE, CharTypes.NUMBER, CharTypes.SPECIAL_CHAR};
}
