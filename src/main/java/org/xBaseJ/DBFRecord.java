package org.xBaseJ;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public interface DBFRecord {
	int recno();
	DBF dbf();
	String get(String field) throws xBaseJException, IOException;
	void put(String field, String value) throws ArrayIndexOutOfBoundsException, xBaseJException, IOException;
	void setDeleted(boolean deleted) throws xBaseJException, IOException;
	boolean isDeleted() throws xBaseJException, IOException;
	default LocalDate getLocalDate(String field) throws xBaseJException, IOException {
		try {
			return LocalDate.parse(get(field), DateTimeFormatter.BASIC_ISO_DATE);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	default void put(String field, LocalDate value) throws ArrayIndexOutOfBoundsException, xBaseJException, IOException {
		put(field, value.format(DateTimeFormatter.BASIC_ISO_DATE));
	}
	default BigDecimal getDecimal(String field) throws xBaseJException, IOException {
		try {
			return new BigDecimal(get(field));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	default void put(String field, BigDecimal value) throws ArrayIndexOutOfBoundsException, xBaseJException, IOException {
		put(field, value.toPlainString());
	}
}
