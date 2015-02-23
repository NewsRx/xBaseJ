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


import java.io.IOException;

import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;

public class testAdd extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(testAdd.class);
	}

	public void testNewCharField() {

		try {
			DBF d1 = new DBF("testfiles/a.dbf", true);
			CharField c = new CharField("C3", 10);
			d1.addField(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testReaddSame() {

		try {
			DBF d1 = new DBF("testfiles/a.dbf");
			CharField c = new CharField("C3", 10);
			d1.addField(c);
			fail("shouldn't be able to add field again");
		} catch (xBaseJException e) {
			;
		}
		catch (IOException ei)
		{
			fail(ei.getMessage());
		}
	}
}
