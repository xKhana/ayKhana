package io.github.xkhana.ayKhana.validation.phone_number;

import io.github.xkhana.ayKhana.util.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private final PhoneNumberUtil phoneUtil;
  private String region;

  @Override
  public void initialize(PhoneNumber constraintAnnotation) {
    this.region = constraintAnnotation.region();
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
    if (region.isEmpty())
      return phoneUtil.isValidNumber(phoneNumber);
    return phoneUtil.isValidNumberForRegion(phoneNumber, region);
  }
}
