package org.xBaseJ.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;

public class Clipper_Large_CharFieldTest {

//	@Test
//	public void testWithBalsetFile() {
//		try {
//			DBF clipperDBF = new DBF("BALSET.DBF");
//			for (int f=1; f <= clipperDBF.getFieldCount(); f++)
//			{
//				System.out.println(clipperDBF.getField(f).toString());
//			}
//			
//			clipperDBF.copyTo("balsetII.dbf");
//		} catch (xBaseJException e) {
//			fail(e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			fail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
	@Test
	public void testBuildNew() {
		try {
			CharField scf = new CharField("short", 10);
			CharField lcf = new CharField("long", 510);
			DBF newone = new DBF("balsetIII.dbf", true);
			newone.addField(scf);
			newone.addField(lcf);
			newone.close();
			newone = new DBF("balsetIII.dbf");
			
			for (int f=1; f <= newone.getFieldCount(); f++)
			{
				System.out.println(newone.getField(f).toString());
			}
			
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 510; i++)
				sb.append('s');
			newone.getField("long").put(sb.toString());
			newone.write();
			newone.close();
			newone = new DBF("balsetIII.dbf");
			newone.read();
			System.out.println(newone.getField("long").get().length());
		} catch (Exception e) {
			fail(e.getMessage());
		} 
		
	}

}
