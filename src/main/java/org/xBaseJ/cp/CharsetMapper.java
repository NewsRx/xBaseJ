package org.xBaseJ.cp;

public interface CharsetMapper {
  char map(char c);

  CharSequence map(CharSequence sequence);

  String map(String string);
}
