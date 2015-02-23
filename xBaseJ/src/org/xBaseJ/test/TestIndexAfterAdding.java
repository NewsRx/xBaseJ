/**
 * 
 */
package org.xBaseJ.test;

import junit.framework.TestCase;

import org.xBaseJ.DBF;
import org.xBaseJ.fields.NumField;

/**
 * @author joseph mcverry
 *
 */
public class TestIndexAfterAdding extends TestCase {

	/**
	 * @param name
	 */
	public TestIndexAfterAdding(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testRun() throws Exception {
        NumField Elm_No = null;
        NumField Hit_Count = null;
        NumField Last_Draw_No = null;

        DBF oDB = new DBF( "testIndexAfterAdding.dbf", true);

        Elm_No          = new NumField( "ElmNo",    2, 0);
        Hit_Count       = new NumField( "HitCount", 6, 0);
        Last_Draw_No    = new NumField( "LstDrwNo", 6, 0);

        oDB.addField(Elm_No);
        oDB.addField(Hit_Count);
        oDB.addField(Last_Draw_No);


        oDB.createIndex("testIndexAfterAdding_elmno.ndx","ElmNo",true,true);

        Elm_No.put( 14);
        Hit_Count.put( 22);
        Last_Draw_No.put( 897);
        oDB.write();

        Elm_No.put( 10);
        Hit_Count.put( 3);
        Last_Draw_No.put( 1);
        oDB.write();

        Elm_No.put( 44);
        Hit_Count.put( 33);
        Last_Draw_No.put( 301);
        oDB.write();

        oDB.close();

        DBF pDB = new DBF( "testIndexAfterAdding.dbf");
        Elm_No       = (NumField) pDB.getField("ElmNo");
        Hit_Count    = (NumField) pDB.getField("HitCount");
        Last_Draw_No = (NumField) pDB.getField("LstDrwNo");

        pDB.useIndex( "testIndexAfterAdding_elmno.ndx");

        System.out.println("first");
        pDB.find( "44");
        assertEquals("44", Elm_No.get());
        System.out.println("second");
        pDB.find( "44");
        assertEquals("44", Elm_No.get());
        
	

	}

}
