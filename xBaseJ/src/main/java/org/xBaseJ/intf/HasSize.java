package org.xBaseJ.intf;

import java.io.IOException;

public interface HasSize {
	public long memoLength() throws IOException;
	public long dbfLength() throws IOException;
}
