package org.wildflowergardening.backend.api.wildflowergardening.util;

public class PhoneNumberFormatter {

  public static String format(String original) {
    return original.replaceAll("-", "")
        .replaceAll(" ", "");
  }
}
