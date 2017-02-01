package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.DBTFile;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.MemoField;

public class NsMemoField extends MemoField {

	public NsMemoField() {
	}

	public NsMemoField(boolean inFoxPro) {
		super(inFoxPro);
	}

	public NsMemoField(String Name, ByteBuffer inBuffer, DBTFile indbtobj) throws xBaseJException, IOException {
		super(Name, inBuffer, indbtobj);
	}

	public NsMemoField(String iName) throws xBaseJException, IOException {
		super(iName);
	}

	public NsMemoField(String iName, boolean inFoxPro) throws xBaseJException, IOException {
		super(iName, inFoxPro);
	}

	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes == null ? new byte[0] : inBytes);
	}

	@Override
	public void put(String inValue) {
		super.put(inValue == null ? "" : inValue);
	}
}
