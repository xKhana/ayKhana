package io.github.xkhana.ayKhana.util;

public interface PhoneNumberUtil {
  boolean isValidNumber(String phoneNumber);

  boolean isValidNumberForRegion(String phoneNumber, String regionCode);
}
