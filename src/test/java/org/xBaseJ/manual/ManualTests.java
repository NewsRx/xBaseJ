package org.xBaseJ.manual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.xBaseJ.DBF;
import org.xBaseJ.Util;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.MemoField;
import org.xBaseJ.xBaseJException;

public class ManualTests {

  public static void main(String[] args) throws IOException, xBaseJException {
    String testFolderSource = "VFP9-test-files";
    String testFolderDest = "temp";
    Util.setxBaseJProperty("ignoreVersionMismatch", "true");
    extracted(testFolderSource, "banned-content-db3.DBF", testFolderDest, "banned-content-db3.dbf");
    extracted(
        testFolderSource, "test-data-with-memo.dbf", testFolderDest, "test-data-with-memo.dbf");
    //		dumpWithMemoField(10);
  }

  //	private static void dumpWithMemoField(int i) {
  //		try (DBF dbf = new DBF(Paths.get(testFolderDest, testFileDest).toFile().getAbsolutePath())) {
  //			System.out.println("Record count: "+dbf.getRecordCount());
  //			for (int ir = 1; ir<=dbf.getFieldCount(); ir++) {
  //				Field field = dbf.getField(ir);
  //				System.out.println("-Field: "+field.Name+"<"+((int)field.getType())+">"+"
  // ["+field.Length+"]");
  //			}
  //		}
  //	}

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
            "-Field: " + field.Name + "<" + (field.getType()) + ">" + " [" + field.Length + "]");
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
