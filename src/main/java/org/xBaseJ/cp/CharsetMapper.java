package org.xBaseJ.cp;

public interface CharsetMapper {
  public char map(char c);

  public CharSequence map(CharSequence sequence);

  public String map(String string);
}
