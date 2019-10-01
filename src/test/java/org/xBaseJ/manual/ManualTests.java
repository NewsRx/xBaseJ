package org.xBaseJ.manual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.xBaseJ.DBF;
import org.xBaseJ.DBFTypes;
import org.xBaseJ.Util;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.MemoField;

public class ManualTests {

  public static void main(String[] args) throws IOException, xBaseJException, SecurityException, CloneNotSupportedException {
    String testFolderSource = "VFP9-test-files";
    String testFolderDest = "temp";
    Util.setxBaseJProperty("ignoreVersionMismatch", "true");
    extracted(testFolderSource, "banned-content-db3.DBF", testFolderDest, "banned-content-db3.dbf");
    extracted(
        testFolderSource, "test-data-with-memo.dbf", testFolderDest, "test-data-with-memo.dbf");
    createDbfsToLoadIntoFoxpro();
    packTest();
  }

  private static void packTest() throws SecurityException, IOException, xBaseJException, CloneNotSupportedException {
	System.out.println("Pack DBASEIII_WITH_MEMO test");
	try (DBF dbf =
	        new DBF("temp/test-dbaseiii-with-memo-pack.dbf", DBFTypes.DBASEIII_WITH_MEMO, true)) {
	      CharField charField = new CharField("charfield", 254);
	      MemoField memoField = new MemoField("memofield");
	      dbf.addField(new Field[] {charField, memoField});
	      for (int count=0; count<10000; count++) {
	    	  charField.put("charfield value "+count);
	    	  memoField.put("memofield value "+count);
	    	  dbf.write();
	      }
	      dbf.gotoRecord(10);
	      dbf.delete();
	      dbf.pack();
	      dbf.gotoRecord(9);
	      System.out.println("charField: "+charField.get().trim());
	      System.out.println("memoField: "+memoField.get().trim());
	      dbf.gotoRecord(10);
	      System.out.println("charField: "+charField.get().trim());
	      System.out.println("memoField: "+memoField.get().trim());
	    }
  }

private static void createDbfsToLoadIntoFoxpro()
      throws SecurityException, xBaseJException, IOException {
    try (DBF dbf =
        new DBF("temp/test-in-foxpro-dbaseiii-with-memo.dbf", DBFTypes.DBASEIII_WITH_MEMO, true)) {
      CharField charField = new CharField("charfield", 254);
      MemoField memoField = new MemoField("memofield");
      dbf.addField(new Field[] {charField, memoField});
      charField.put("charfield value");
      memoField.put("memofield value");
      dbf.write();
    }
    try (DBF dbf =
        new DBF("temp/test-in-foxpro-dbaseiv-with-memo.dbf", DBFTypes.DBASEIV_WITH_MEMO, true)) {
      CharField charField = new CharField("charfield", 254);
      MemoField memoField = new MemoField("memofield");
      dbf.addField(new Field[] {charField, memoField});
      charField.put("charfield value");
      memoField.put("memofield value");
      dbf.write();
    }
    try (DBF dbf =
        new DBF("temp/test-in-foxpro-foxpro2-with-memo.dbf", DBFTypes.FOXPRO_WITH_MEMO, true)) {
      CharField charField = new CharField("charfield", 254);
      MemoField memoField = new MemoField("memofield");
      dbf.addField(new Field[] {charField, memoField});
      charField.put("charfield value");
      memoField.put("memofield value");
      dbf.write();
    }
  }

  private static void extracted(
      String testFolderSource, String testFileSource, String testFolderDest, String testFileDest)
      throws IOException, xBaseJException {
    Path srcPath = Paths.get(testFolderSource, testFileSource);
    Path destPath = Paths.get(testFolderDest, testFileDest);
    Files.copy(
        srcPath, destPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    if (testFileSource.endsWith(".dbf")) {
      Path memoPath = srcPath.resolveSibling(testFileSource.replace(".dbf", ".fpt"));
      if (memoPath.toFile().exists()) {
        Files.copy(
            memoPath,
            destPath.resolveSibling(testFileDest.replace(".dbf", ".fpt")),
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES);
      }
    }
    if (testFileSource.endsWith(".DBF")) {
      Path memoPath = srcPath.resolveSibling(testFileSource.replace(".DBF", ".FPT"));
      if (memoPath.toFile().exists()) {
        Files.copy(
            memoPath,
            destPath.resolveSibling(testFileDest.replace(".DBF", ".FPT")),
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES);
      }
    }

    System.out.println();
    System.out.println("=== " + destPath.toFile().getAbsolutePath());
    try (DBF dbf = new DBF(destPath.toFile().getAbsolutePath())) {
      System.out.println("Record count: " + dbf.getRecordCount());
      CharField f1 = null;
      MemoField m1 = null;
      System.out.println("--- Fields");
      for (int ir = 1; ir <= dbf.getFieldCount(); ir++) {
        Field field = dbf.getField(ir);
        System.out.println(
            "-Field: " + field.Name + "<" + field.getType() + ">" + " [" + field.Length + "]");
        if (f1 == null && field.getType() == CharField.type) {
          f1 = (CharField) field;
        }
        if (m1 == null && field.getType() == MemoField.type) {
          m1 = (MemoField) field;
        }
      }
      System.out.println();
      System.out.println("--- Data");
      for (int recno = 1; recno <= dbf.getRecordCount() && recno <= 3; recno++) {
        dbf.gotoRecord(recno);
        System.out.println(
            recno
                + ": "
                + (f1 == null ? "null" : f1.getName() + "='" + f1.get() + "'")
                + ", "
                + (m1 == null ? "null" : m1.getName() + "='" + m1.get() + "'"));
      }
    }
  }
}
