package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.FloatField;

public class NsFloatField extends FloatField {

	public NsFloatField() {
	}

	public NsFloatField(String iName, int iLength, int DecPoint, ByteBuffer inBuffer) throws xBaseJException {
		super(iName, iLength, DecPoint, inBuffer);
	}

	public NsFloatField(String iName, int iLength, int DecPoint) throws xBaseJException, IOException {
		super(iName, iLength, DecPoint);
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
