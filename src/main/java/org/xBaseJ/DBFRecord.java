package org.xBaseJ;

import java.io.IOException;

public interface DBFRecord {
	int recno();
	DBF dbf();
	String get(String field) throws xBaseJException, IOException;
	void put(String field, String value) throws ArrayIndexOutOfBoundsException, xBaseJException, IOException;
	void setDeleted(boolean deleted) throws xBaseJException, IOException;
	boolean isDeleted() throws xBaseJException, IOException;
}
