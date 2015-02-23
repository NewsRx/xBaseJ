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
import org.xBaseJ.fields.MemoField;


public class testDBF extends TestCase {

	public void testBuildDBF() {
		DBF aDB = null;
		try {
			aDB = new DBF("testfiles/testdbt.dbf", true);
		} catch (SecurityException e) {
			fail(e.getMessage());
		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		CharField cf = null;
		try {
			cf = new CharField("char", 10);
		} catch (xBaseJException e) {
			fail(e.getMessage());

		} catch (IOException e) {
			fail(e.getMessage());

		}
		MemoField mf = null;
		try {
			mf = new MemoField("memo");
		} catch (xBaseJException e) {
			fail(e.getMessage());

		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			aDB.addField(cf);
		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		try {
			aDB.addField(mf);
		} catch (xBaseJException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			aDB.close();
		} catch (IOException e) {
			fail(e.getMessage());

		}

		try {
			aDB = new DBF("testfiles/testdbt.dbf");
			cf = (CharField) aDB.getField("char");
			mf = (MemoField) aDB.getField("memo");
			cf.put("123456789");
			mf.put("123456789");

			aDB.write();

			cf.put("9");
			mf.put("9");

			aDB.write();

			aDB.close();
		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			aDB = new DBF("testfiles/testdbt.dbf");

			cf = (CharField) aDB.getField("char");
			mf = (MemoField) aDB.getField("memo");

			aDB.read();

			String s = cf.get();
			assertEquals("123456789", s);

			s = mf.get();
			assertEquals("123456789", s);

			aDB.read();

			s = cf.get();
			assertEquals("9", s);

			s = mf.get();
			assertEquals("9", s);

		} catch (xBaseJException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}
}
