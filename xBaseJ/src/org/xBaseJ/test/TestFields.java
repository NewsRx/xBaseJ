package org.xBaseJ.test;
/**
 * xBaseJ - Java access to dBase files
 *<p>Copyright 1997-2014 - American Coders, LTD  - Raleigh NC USA
 *<p>All rights reserved
 *<p>Currently supports only dBase III format DBF, DBT and NDX files
 *<p>                        dBase IV format DBF, DBT, MDX and NDX files
*<p>American Coders, Ltd
*<br>P. O. Box 97462
*<br>Raleigh, NC  27615  USA
*<br>1-919-846-2014
*<br>http://www.americancoders.com
@author Joe McVerry, American Coders Ltd.
@Version 20140310
*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library Lesser General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
*/


import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.CurrencyField;
import org.xBaseJ.fields.DateField;
import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.NumField;
import org.xBaseJ.fields.PictureField;


public class TestFields extends TestCase {



	CharField f;

	public  void setUp(){
		try {
			 f = new CharField("test", 10);
		} catch (xBaseJException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

}


	/*
	 * Test method for 'org.xBaseJ.Field.put(String)'
	 */
	public void testPutString() {
		try {
			f.put("a");
		} catch (xBaseJException e) {
			fail(e.getMessage());
		}
		TestFields.assertEquals("a",f.get());

	}
	
	public void testType()  {
		try {
			CharField  c = new CharField("C", 1);
			assertEquals('C', c.getType());
			DateField  d = new DateField("D");
			assertEquals('D', d.getType());
			FloatField f = new FloatField("F", 10, 2); 
			assertEquals('F', f.getType());
			NumField n = new NumField("N", 10, 2);
			assertEquals('N', n.getType());
			LogicalField l = new LogicalField("L");
			assertEquals('L', l.getType());
			PictureField p = new PictureField("P");
			assertEquals('P', p.getType());
			CurrencyField cc = new CurrencyField("Money");
			assertEquals('Y', cc.getType());
			
		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	
	public void testFloat() {
	    try {
		DBF db = new DBF("testfiles/float.dbf", true);
		FloatField f = new FloatField("F", 10,3);
		db.addField(f);
		f.put(987.123f);
		db.write();
		db.close();
		db = new DBF("testfiles/float.dbf");
		f = (FloatField) db.getField("F");
		db.read();
		assertEquals("   987.123", f.get());
		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		finally {
		    File f = new File("testfiles/float.dbf");
		    f.delete();
		}
	}

}
