package io.github.xkhana.ayKhana.validation.phone_number;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
  String message() default "{validation.phoneNumber}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String region() default "";
}
