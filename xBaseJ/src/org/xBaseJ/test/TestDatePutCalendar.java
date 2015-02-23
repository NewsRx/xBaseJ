package org.xBaseJ.test;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.DateField;

public class TestDatePutCalendar  extends TestCase{

	public void testDatePutCalendar() {
		try {
			DateField df = new DateField("test");
			df.put(Calendar.getInstance());
			df.getCalendar(TimeZone.getDefault());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (xBaseJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

	public void testDatePutDate() {
		try {
			DateField df = new DateField("test");
			df.put(Calendar.getInstance().getTime());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (xBaseJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	   
}
