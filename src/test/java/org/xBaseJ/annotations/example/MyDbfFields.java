package org.xBaseJ.annotations.example;

import org.xBaseJ.annotations.DBFField;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.CurrencyField;
import org.xBaseJ.fields.DateField;
import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.MemoField;
import org.xBaseJ.fields.NumField;
import org.xBaseJ.fields.PictureField;

public class MyDbfFields {
	@DBFField(name="mychar", size=32)
	protected CharField myCharField;
	
	@DBFField(name="money")
	protected CurrencyField myMoney;
	
	@DBFField(name="date")
	protected DateField myDate;

	@DBFField(name="float")
	protected FloatField myFloat;
	
	@DBFField(name="logical")
	protected LogicalField logical;
	
	@DBFField(name="memo")
	protected MemoField myMemo;
	
	@DBFField(name="number44", size=4, dec=4)
	protected NumField number44;
	
	@DBFField(name="number40", size=4)
	protected NumField number40;
	
	@DBFField(name="number100", size=10)
	protected NumField number100;
	
	@DBFField(name="picture")
	protected PictureField picture;
	
	
}
