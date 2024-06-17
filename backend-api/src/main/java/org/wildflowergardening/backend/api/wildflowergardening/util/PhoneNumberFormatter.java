package org.wildflowergardening.backend.api.wildflowergardening.util;

public class PhoneNumberFormatter {

  public static String format(String original) {
    if (original == null) {
      return null;
    }
    return original.replaceAll("-", "")
        .replaceAll(" ", "");
  }
}
