package io.github.xkhana.ayKhana.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntPredicate;

public class StrongPasswordValidator implements ConstraintValidator<Password, String> {
  private int min;
  private EnumSet<CharTypes> shouldContain;

  private static boolean isSpecialChar(int c) {
    return "/*!@#$%^&*()\"{}_[]|\\?/<>,.".indexOf(c) >= 0;
  }

  @Override
  public void initialize(Password constraintAnnotation) {
    min = constraintAnnotation.min();
    shouldContain = EnumSet.copyOf(Arrays.asList(constraintAnnotation.shouldContain()));
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    AtomicBoolean isValid = new AtomicBoolean(true);

    constraintValidatorContext.disableDefaultConstraintViolation();

    if (s == null || s.isBlank()) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("EMPTY_PASSWORD").addConstraintViolation();
      return false;
    }

    if (s.length() < min) {
      constraintValidatorContext.buildConstraintViolationWithTemplate("PASSWORD_TOO_SHORT").addConstraintViolation();
      isValid.set(false);
    }

    record Constraint(
        String name,
        IntPredicate predicate,
        CharTypes type
    ) {
    }

    List<Constraint> constraints = List.of(
        new Constraint("SHOULD_CONTAIN_DIGIT", Character::isDigit, CharTypes.NUMBER),
        new Constraint("SHOULD_CONTAIN_SPECIAL_CHAR", StrongPasswordValidator::isSpecialChar, CharTypes.SPECIAL_CHAR),
        new Constraint("SHOULD_CONTAIN_LETTER", Character::isLetter, CharTypes.LETTER),
        new Constraint("SHOULD_CONTAIN_LOWER_CASE", Character::isLowerCase, CharTypes.LOWERCASE),
        new Constraint("SHOULD_CONTAIN_UPPER_CASE", Character::isUpperCase, CharTypes.UPPERCASE)
    );

    constraints.forEach(constraint -> {
      if (shouldContain.contains(constraint.type) && s.chars().noneMatch(constraint.predicate)) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(constraint.name).addConstraintViolation();
        isValid.set(false);
      }
    });

    return isValid.get();
  }
}
