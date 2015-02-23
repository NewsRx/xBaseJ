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

import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.DBFTypes;
import org.xBaseJ.fields.CharField;


public class TestMultiAdd extends TestCase {


	public void testMultipleFieldAdd() {
		CharField zip = null;

		CharField preDir = null;

		CharField street = null;

		CharField suffix = null;

		CharField postDir = null;

		CharField term = null;

		String zipFieldStr = "ZIP";

		String preDirFieldStr = "PREDIR";

		String streetFieldStr = "STREET";

		String suffixFieldStr = "SUFFIX";

		String postDirFieldStr = "POSTDIR";

		String termFieldStr = "TERM";


		File File = new File("testfiles/test.dbf");
		try {

		DBF dbf = new DBF(File.getPath(), DBFTypes.DBASEIV, true);

		zip = new CharField(zipFieldStr, 5);
		dbf.addField(zip);
		zip.put("12345");
		dbf.write();

		preDir = new CharField(preDirFieldStr, 2);
		dbf.addField(preDir);
		preDir.put("12");
		dbf.write();

		street = new CharField(streetFieldStr, 28);
		dbf.addField(street);
		street.put("12345 through 28");
		dbf.write();

		suffix = new CharField(suffixFieldStr, 4);
		dbf.addField(suffix);
		suffix.put("1234");
		dbf.write();

		postDir = new CharField(postDirFieldStr, 2);
		dbf.addField(postDir);
		postDir.put("12");
		dbf.write();

		term = new CharField(termFieldStr, 5);
		dbf.addField(term);
		term.put("12345");
		dbf.write();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());

		}


	}

}