package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.DBTFile;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.PictureField;

public class NsPictureField extends PictureField {

	public NsPictureField() {
	}

	public NsPictureField(String Name, ByteBuffer inBuffer, DBTFile indbtobj) throws xBaseJException, IOException {
		super(Name, inBuffer, indbtobj);
	}

	public NsPictureField(String iName) throws xBaseJException, IOException {
		super(iName);
	}

	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes == null ? new byte[0] : inBytes);
	}

	@Override
	public void put(String invalue) throws xBaseJException {
		super.put(invalue == null ? "" : invalue);
	}

}
