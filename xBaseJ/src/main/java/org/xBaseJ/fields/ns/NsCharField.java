package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;

public class NsCharField extends CharField {

	public NsCharField() {
	}

	public NsCharField(String iName, int iLength, ByteBuffer inBuffer) throws xBaseJException, IOException {
		super(iName, iLength, inBuffer);
	}

	public NsCharField(String iName, int iLength) throws xBaseJException, IOException {
		super(iName, iLength);
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
