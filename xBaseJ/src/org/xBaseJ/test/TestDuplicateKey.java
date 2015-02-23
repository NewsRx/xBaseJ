/**
 * is there a known bug regarding find_rec(NodeKey, Node, int) in NDX.java 
 * while updating a record with non-unique index? 
 * In my oppinion this line: 
 *    for (aNode.set_pos(0); aNode.get_pos() < until && stat > 0; aNode.pos_up())
 *     (see full method attached)
 * is handling "stat" not appropiately. 
 * When stat is 0, but the record found is not the one we are searching for, there is no loop! 
 * We will find the record we are looking for if this line goes like: 
 *     for (aNode.set_pos(0); aNode.get_pos() < until && stat >= 0; aNode.pos_up()) 
 * Result: A new entry is being added even if there IS a matching record (=> duplicate entries)!
 */
package org.xBaseJ.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.xBaseJ.DBF;
 
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.Field;
 






import junit.framework.TestCase;

/**
 * @author joe
 *
 */
public class TestDuplicateKey extends TestCase {

	/**
	 * @param name
	 */
	public TestDuplicateKey(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		File f = new File("testfiles/testupdidx.ndx");
		f.delete();
		f = new File("testfiles/testupdidx.dbf");
		f.delete();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
	
	public void test() throws Exception {
		DBF db = new DBF("testfiles/testupdidx.dbf", true);
		CharField c1 = new CharField("first", 2);
		CharField c2 = new CharField("second", 20);
		Field f[] = {c1, c2};
		db.addField(f);
		db.createIndex("testfiles/testupdidx.ndx", "first", false);
		db.close();
		
		
		db = new DBF("testfiles/testupdidx.dbf");
		db.useIndex("testfiles/testupdidx.ndx");
		c1 = (CharField) db.getField("first");
		c2 = (CharField) db.getField("second");
		c1.put("11");
		c2.put("first");
		db.write();
		c1.put("11");
		c2.put("second");
		db.write();
		c1.put("11");
		c2.put("third");
		db.write(); // all three rows have the same value
		db.startTop();
		ArrayList<String> secondValues = new ArrayList<String>();
		for (int lp = 0; lp < 3; lp++)
		{
			db.findNext();
			secondValues.add(c2.get());
		}
		db.close();
//		System.exit(0);
		db = new DBF("testfiles/testupdidx.dbf");
		c1 = (CharField) db.getField("first");
		c2 = (CharField) db.getField("second");
		db.useIndex("testfiles/testupdidx.ndx");
		db.find("11");
		int foundCnt = 0;
		HashSet<String> secondVHash = new HashSet<String>(secondValues);
		for (int lp = 0; lp < 3; lp++) {
			if (secondVHash.contains(c2.get())) {
				if (c2.get().equals("second")) {
					c2.put("2nd updated");
					db.update();
				}
					
				secondVHash.remove(c2.get());
				foundCnt++;
			}
			else {
				fail("can't find "+c2.get());
			}
			if (lp < 2)
				db.findNext();
		}
		assertEquals(3, foundCnt);
		secondValues = new ArrayList<String>();
		db.startTop();
		for (int lp = 0; lp < 3; lp++)
		{
			System.out.println(c2.get());
			secondValues.add(c2.get());
			if (lp < 2)
				db.findNext();
		}
		db.close();
		
		secondVHash = new HashSet<String>(secondValues);
		db = new DBF("testfiles/testupdidx.dbf");
		assertEquals(3, db.getRecordCount());
		c1 = (CharField) db.getField("first");
		c2 = (CharField) db.getField("second");
		db.useIndex("testfiles/testupdidx.ndx");
		foundCnt = 0;
		db.find("11");
		
		for (int lp = 0; lp < 3; lp++) {
			if (secondVHash.contains(c2.get())) {
				secondVHash.remove(c2.get());
				foundCnt++;
			}
			else {
				fail("can't find "+c2.get());
			}
			if (lp < 2)
				db.findNext();
		}
		assertEquals(3, foundCnt);

	}


}
