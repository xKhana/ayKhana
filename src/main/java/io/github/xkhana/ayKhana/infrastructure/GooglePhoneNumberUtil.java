package io.github.xkhana.ayKhana.infrastructure;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;
import io.github.xkhana.ayKhana.util.PhoneNumberUtil;
import org.springframework.stereotype.Component;

@Component
public class GooglePhoneNumberUtil implements PhoneNumberUtil {

  com.google.i18n.phonenumbers.PhoneNumberUtil phoneUtil = com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance();

  @Override
  public boolean isValidNumber(String phoneNumber) {
    Phonenumber.PhoneNumber phonenumber = null;
    try {
      phonenumber = phoneUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
      return phoneUtil.isValidNumber(phonenumber);
    } catch (NumberParseException e) {
      return false;
    }
  }

  @Override
  public boolean isValidNumberForRegion(String phoneNumber, String regionCode) {
    Phonenumber.PhoneNumber phonenumber = null;
    try {
      phonenumber = phoneUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
      return phoneUtil.isValidNumberForRegion(phonenumber, regionCode);
    } catch (NumberParseException e) {
      return false;
    }
  }
}
