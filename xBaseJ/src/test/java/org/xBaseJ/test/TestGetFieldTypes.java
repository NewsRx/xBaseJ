package org.xBaseJ.test;

import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.CurrencyField;
import org.xBaseJ.fields.DateField;
import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.MemoField;
import org.xBaseJ.fields.NumField;
import org.xBaseJ.fields.PictureField;

import junit.framework.TestCase;

public class TestGetFieldTypes extends TestCase {

	public void testCharField() throws Exception{
		CharField f = new CharField("a", 1);
		assertEquals(true, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testDateField() throws Exception{
		DateField f = new DateField("a");
		assertEquals(false, f.isCharField());
		assertEquals(true, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testFloatField() throws Exception{
		FloatField f = new FloatField("a", 10, 2);
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(true, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testLogicalField() throws Exception{
		LogicalField f = new LogicalField("a");
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(true, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testMemoField() throws Exception{
		MemoField f = new MemoField("a");
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(true, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testNumField() throws Exception{
		NumField f = new NumField("a", 10, 2);
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(true, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testPictureField() throws Exception{
		PictureField f = new PictureField("a");
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(true, f.isPictureField());
		assertEquals(false, f.isCurrencyFIeld());
	}
	public void testCurrencyField() throws Exception{
		CurrencyField f = new CurrencyField("a");
		assertEquals(false, f.isCharField());
		assertEquals(false, f.isDateField());
		assertEquals(false, f.isFloatField());
		assertEquals(false, f.isLogicalField());
		assertEquals(false, f.isMemoField());
		assertEquals(false, f.isNumField());
		assertEquals(false, f.isPictureField());
		assertEquals(true, f.isCurrencyFIeld());
	}
}
