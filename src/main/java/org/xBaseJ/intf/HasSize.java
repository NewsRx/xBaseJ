package org.xBaseJ.intf;

import java.io.IOException;

public interface HasSize {
  long memoLength() throws IOException;

  long dbfLength() throws IOException;
}
