package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.LogicalField;

public class NsLogicalField extends LogicalField {

	public NsLogicalField() {
	}

	public NsLogicalField(String iName, ByteBuffer inBuffer) throws xBaseJException {
		super(iName, inBuffer);
	}

	public NsLogicalField(String iName) throws xBaseJException, IOException {
		super(iName);
	}
	
	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes==null?new byte[0]:inBytes);
	}
	
	@Override
	public void put(String inValue) throws xBaseJException {
		super.put(inValue==null?"":inValue);
	}

}
