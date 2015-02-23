package org.xBaseJ.test;

import java.io.PrintWriter;

import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.XBASEXMLParser;


public class TestXMLParser  extends TestCase{

	
	public void testXMLParserRoundTrip() throws Exception{
		
		DBF sdf = new DBF("testfiles/testdbt.dbf");
		
		PrintWriter pw = new PrintWriter("testfiles/testdbtdbf.xml");
		sdf.getXML(pw);
		XBASEXMLParser xxp = new XBASEXMLParser();
		xxp.parse("testfiles/testdbtdbf.xml", "testfiles/testdbtxmled.dbf");
		
		DBF xscf = new DBF("testfiles/testdbtxmled.dbf");
		
		assertEquals(sdf.getFieldCount(), xscf.getFieldCount());
		      for (int f = 1; f <= sdf.getFieldCount(); f++) {
			  assertEquals(sdf.getField(f).getName(), xscf.getField(f).getName());
			  assertEquals(sdf.getField(f).getLength(), xscf.getField(f).getLength());
			  assertEquals(sdf.getField(f).getType(), xscf.getField(f).getType());
		      }
		assertEquals(sdf.getRecordCount(), xscf.getRecordCount());
		for (int i = 0; i < sdf.getIndexCount(); i++) {
		      sdf.read();
		      xscf.read();
		      for (int f = 1; f <= sdf.getFieldCount(); f++) {
			  assertEquals(sdf.getField(f).get(), xscf.getField(f).get());
		      }
		}
		
		
		
	}
}
