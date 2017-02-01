/**
 * 
 */
package org.xBaseJ.test;

import junit.framework.Test;
import junit.framework.TestSuite;
 
/**
 * @author joe
 *
 */
public class AllxBaseJTests {

	public static Test suite() {



		TestSuite suite = new TestSuite("Test for org.xBaseJ.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestDuplicateKey.class);
		suite.addTestSuite(TestPack.class);
		suite.addTestSuite(TestGetFieldTypes.class);
		suite.addTestSuite(FieldNameTest.class);
		suite.addTestSuite(MissingMDX.class);
		suite.addTestSuite(NumTest.class);
		suite.addTestSuite(testAdd.class);
		suite.addTestSuite(testCreate.class);
		suite.addTestSuite(testDBF.class);
		suite.addTestSuite(TestFields.class);
		//suite.addTestSuite(TestFoxPro.class);
		suite.addTestSuite(TestLock.class);
		suite.addTestSuite(TestLockRead.class);
		suite.addTestSuite(TestLockUpdateClose.class);
		suite.addTestSuite(testMaxLength.class);
		suite.addTestSuite(TestMultiAdd.class);
		suite.addTestSuite(TestNoBlanks.class);
		suite.addTestSuite(TestPack.class);
		suite.addTestSuite(TestConcurrInsert.class);
		suite.addTestSuite(TestIndexAfterAdding.class);
		suite.addTestSuite(TestXMLParser.class);
		suite.addTestSuite(TestDatePutCalendar.class);
		return suite;
	}

	public static void main(StringBuffer args[]) {
		junit.textui.TestRunner.run(suite());
	}

}
