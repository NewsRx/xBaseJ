package org.xBaseJ.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.LogicalField;

public class TestLogical {

	@Test
	public void test() {
		try {
			LogicalField lf = new LogicalField("a");
			lf.put("");
			assertFalse(lf.getBoolean());
			lf.put("f");
			assertFalse(lf.getBoolean());
			lf.put("F");
			assertFalse(lf.getBoolean());
			lf.put("T");
			assertTrue(lf.getBoolean());
			lf.put("t");
			assertTrue(lf.getBoolean());
			lf.put("TRue");
			assertTrue(lf.getBoolean());
			lf.put("true");
			assertTrue(lf.getBoolean());
			lf.put("false");
			assertFalse(lf.getBoolean());
			lf.put("FALSE");
			assertFalse(lf.getBoolean());
			lf.put("Tuer"); // misspelled true is false
			assertFalse(lf.getBoolean());
			lf.put(true);
			assertTrue(lf.getBoolean());
			lf.put(false);
			assertFalse(lf.getBoolean());
		} catch (xBaseJException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
