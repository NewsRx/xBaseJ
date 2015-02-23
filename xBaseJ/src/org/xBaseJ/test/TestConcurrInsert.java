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
 * 
 *  Change History
 *  Date      Developer                 Desc
 *  20121217  jrm   Added synchronized block around System.out.println statement in thread
 *
*/


import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.NumField;

public class TestConcurrInsert extends TestCase {

	public void testConncur()
	{
	    try {
		      // set the lock property
		      org.xBaseJ.Util.setxBaseJProperty("useSharedLocks", "false");

		      // create previously a dbf
		      DBF writer = new DBF("testfiles/concurr.dbf", true);
		      Field str_field = new CharField("thread", 15);
		      Field int_field = new NumField("rownum", 5, 0);
		      writer.addField(str_field);
		      writer.addField(int_field);
		      // add a row
		      str_field.put("main thread");
		      int_field.put("-1");
		      writer.write();
		      writer.close();

		      // the first thread that will insert rows with the index=1
		      Thread thread1 = new ConcurrInsert(1);
		      // the second thread that will insert rows with the index=2
		      Thread thread2 = new ConcurrInsert(2);
		      Thread thread3 = new ConcurrInsert(3);

		      // start threads
		      thread1.start();
		      thread2.start();
		      thread3.start();
		      while (thread1.isAlive() || thread2.isAlive() || thread3.isAlive()){Thread.sleep(100);}
		    }
		    catch (Exception ex) {
		    	ex.printStackTrace();
		      fail(ex.getMessage());
		    }
		     DBF reader;
			try {
				reader = new DBF("testfiles/concurr.dbf");
			     assertEquals(3*49+1, reader.getRecordCount());
			}
		    catch (Exception ex) {
					      fail(ex.getMessage());
					    }
		  }





	private class ConcurrInsert extends Thread{
		  private int threadIndex = -1;
		  private DBF writer = null;

		  public ConcurrInsert(int threadIndex) throws Exception{
		    // open a DBF file
		     writer = new DBF("testfiles/concurr.dbf");
		     this.threadIndex = threadIndex;
		  }

	public void run() {
	    try {
		// ### thread adds new rows ###
		Field str_field = writer.getField(1);
		Field int_field = writer.getField(2);

		for (int i = 0; i < 49; i++) {
		    int_field.put(String.valueOf(i));
		    str_field.put("Thread" + threadIndex);
		    int_field.put("" + i);
		    writer.lock();
		    writer.write(true);
		    writer.unlock();
		    sleep(100);
		    synchronized (TestConcurrInsert.class) {
			    System.out.println("Thread " + threadIndex
				    + " has inserted " + i + " row");
		    }
		}
		// close a dbf
		writer.close();
		System.out.println("done "+threadIndex);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

}
