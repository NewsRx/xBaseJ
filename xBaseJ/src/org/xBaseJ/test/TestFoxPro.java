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
import org.xBaseJ.fields.MemoField;

public class TestFoxPro extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestFoxPro.class);
    }


//public void testStart()
//{ i lost the copy of memofile.dbf
//    try{
//        DBF fp = new DBF("testfiles/memofile.dbf");
//        System.out.println(fp.getVersion());
//       }
//    catch (Exception e)
//    {
//       fail(e.getMessage());
//    }
//}
public void testCreateAll() {
	try {
		
		DBF fp = new DBF("testfiles/foxprotest.dbf", DBFTypes.FOXPRO_WITH_MEMO, true);
		fp.addField(new CharField("name", 10));
		fp.addField(new MemoField("memo"));
		fp.close();
		File f = new File("testfiles/foxprotest.dbf");
		if (f.exists() == false)
			fail("can't find foxpro dbf file");
		f = new File("testfiles/foxprotest.fpt");
		if (f.exists() == false)
			fail("can't find foxpro fpt file");
			
	}
	catch (Exception e) {
		e.printStackTrace();
		fail(e.getMessage());
	} 
	
}


}
