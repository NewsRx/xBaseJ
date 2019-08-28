package org.xBaseJ.cp;

public enum CodePage {
  /**
   * See <em>Code Pages Supported by Visual FoxPro</em><br>
   * REF: {@linkplain https://msdn.microsoft.com/en-us/library/8t45x02s(v=vs.71).aspx} and
   * {@linkplain https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html}
   */
  NO_CODEPAGE((byte) 0x0, "UTF-8"),
  US_MSDOS((byte) 0x1, "Cp437"),
  MAZOVIA_MSDOS((byte) 0x69, "?"), //
  GREEK_MSDOS((byte) 0x6a, "Cp737"),
  INTERNATIONAL_MSDOS((byte) 0x02, "Cp850"), //
  EAST_EUROPE_MSDOS((byte) 0x64, "Cp852"), //
  TURKISH_MSDOS((byte) 0x6b, "Cp857"),
  ICELANDIC_MSDOS((byte) 0x67, "Cp861"), //
  NORDIC_MSDOS((byte) 0x66, "Cp865"), //
  RUSSIAN_MSDOS((byte) 0x65, "Cp866"),
  THAI_WINDOWS((byte) 0x7c, "Cp874"),
  CZECH_MSDOS((byte) 0x68, "?"), //
  JAPANESE_WINDOWS((byte) 0x7b, "MS932"),
  CHINESE_PRC_WINDOWS((byte) 0x7a, "MS936"), //
  KOREAN_WINDOWS((byte) 0x79, "MS949"), //
  CHINESE_SAR_WINDOWS((byte) 0x78, "Cp950"),
  EAST_EUROPE_WINDOWS((byte) 0xc8, "Cp1250"), //
  RUSSIAN_WINDOWS((byte) 0xc9, "Cp1251"), //
  WINDOWS_ANSI((byte) 0x03, "Cp1252"),
  GREEK_WINDOWS((byte) 0xcb, "Cp1253"), //
  TURKISH_WINDOWS((byte) 0xca, "Cp1254"), //
  HEBREW_WINDOWS((byte) 0x7d, "Cp1255"),
  ARABIC_WINDOWS((byte) 0x7e, "Cp1256"),
  STANDARD_MAC((byte) 0x04, "?"), //
  GREEK_MAC((byte) 0x98, "MacGreek"),
  RUSSIAN_MAC((byte) 0x96, "?"),
  MAC_EE((byte) 0x97, "?");

  private final byte code_page_identifier;
  private final String java_code_page;

  private CodePage(byte code_page_identifier, String java_code_page) {
    this.code_page_identifier = code_page_identifier;
    this.java_code_page = java_code_page;
  }

  public String getJava_code_page() {
    return java_code_page;
  }

  public byte getCode_page_identifier() {
    return code_page_identifier;
  }
}
