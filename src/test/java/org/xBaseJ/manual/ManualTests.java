package org.xBaseJ.manual;

import java.awt.image.ReplicateScaleFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.xBaseJ.DBF;
import org.xBaseJ.Util;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;

public class ManualTests {

	public static void main(String[] args) throws IOException, xBaseJException {
		String testFolderSource = "VFP9-test-files";
		String testFolderDest = "temp";
		Util.setxBaseJProperty("ignoreVersionMismatch", "true");
		extracted(testFolderSource, "banned-content-db3.DBF", testFolderDest, "banned-content-db3.dbf");
		extracted(testFolderSource, "banned_content.DBF", testFolderDest, "banned_content.dbf");
	}

	private static void extracted(String testFolderSource, String testFileSource, String testFolderDest,
			String testFileDest) throws IOException, xBaseJException {
		System.out.println("Copy: "+Paths.get(testFolderSource, testFileSource).toAbsolutePath().toString()+" -> "+Paths.get(testFolderDest, testFileDest).toAbsolutePath().toString());
		System.out.println("- "+Files.copy(Paths.get(testFolderSource, testFileSource), Paths.get(testFolderDest, testFileDest), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES).toAbsolutePath().toString());
		try (DBF dbf = new DBF(Paths.get(testFolderDest, testFileDest).toFile().getAbsolutePath())) {
			System.out.println("Record count: "+dbf.getRecordCount());
			for (int ir = 1; ir<=dbf.getFieldCount(); ir++) {
				Field field = dbf.getField(ir);
				System.out.println("-Field: "+field.Name+"<"+((int)field.getType())+">"+" ["+field.Length+"]");
			}
		}
	}

}
