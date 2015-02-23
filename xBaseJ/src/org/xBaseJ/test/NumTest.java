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


import junit.framework.TestCase;

import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.NumField;

public class NumTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(NumTest.class);
    }


    public void testFDouble() {
    	try {
            NumField nf = new NumField("name", 7, 3);
            double a = 987.123f;
            nf.put(a);
            assertEquals(nf.get(), "987.123");
            NumField numField = new NumField("N", 7,3);
            FloatField floatField = new FloatField("F", 7,3);
            float floatValue = 987.123f;
            System.out.println("intial value before write = "+floatValue);
            numField.put(floatValue);
            assertEquals(numField.get(), "987.123");
            floatField.put(floatValue);
            assertEquals(floatField.get(), "987.123");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    public void testDouble()
    {
        try {
            NumField nf = new NumField("name", 6, 2);
            double a = -50000000.36;
            nf.put(a);
            assertEquals(nf.get(), "-00.36");
            a = 50000000.36;
            nf.put(a);
            assertEquals(nf.get(), "000.36");
            a = -.36;
            nf.put(a);
            assertEquals(nf.get(), "  -.36");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    public void testNull()
    {
    	try {
    		NumField nf = new NumField("name", 6, 2);
    		nf.put("");
    		assertEquals(nf.get(), "");
    	}
    	catch (Exception e) {
    		fail(e.getMessage());
    	}
    }

}
